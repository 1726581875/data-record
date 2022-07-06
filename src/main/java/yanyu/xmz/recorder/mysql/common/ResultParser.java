package yanyu.xmz.recorder.mysql.common;

import com.mysql.cj.protocol.a.NativeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yanyu.xmz.recorder.mysql.channel.ChannelManager;
import yanyu.xmz.recorder.mysql.channel.MyPacketChannel;
import yanyu.xmz.recorder.mysql.common.util.ByteConvertUtil;
import yanyu.xmz.recorder.mysql.protocol.CapabilityFlags;
import yanyu.xmz.recorder.mysql.protocol.io.ByteArrayInputStreamReader;
import yanyu.xmz.recorder.mysql.protocol.packet.EofPacket;
import yanyu.xmz.recorder.mysql.protocol.packet.ErrPacket;
import yanyu.xmz.recorder.mysql.protocol.packet.OkPacket;
import yanyu.xmz.recorder.mysql.protocol.packet.resultset.ColumnDefinition;
import yanyu.xmz.recorder.mysql.protocol.packet.resultset.ColumnDefinition320;
import yanyu.xmz.recorder.mysql.protocol.packet.resultset.ColumnDefinition41;
import yanyu.xmz.recorder.mysql.protocol.packet.resultset.ResultSetRow;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/7/4
 */
public class ResultParser {

    private static final Logger log = LoggerFactory.getLogger(ResultParser.class);


    /**
     * 解析简单Packet
     * @param resultBytes
     * @return
     * @throws IOException
     */
    public static ResultPacket parseResultPacket(byte[] resultBytes, CapabilityFlags capabilityFlags) throws IOException {
        ResultPacket resultPacket = getSimpleResultPacket(resultBytes, capabilityFlags);
        if(resultPacket != null) {
            return resultPacket;
        }

        throw new RuntimeException("未知类型的Packet");
    }

    /**
     * 解析COM_QUERY命令结果Packet
     * https://dev.mysql.com/doc/internals/en/com-query-response.html
     * @param channelManager
     * @param isComFieldList
     * @throws IOException
     */
    public static ResultPacket parseComQueryResult(ChannelManager channelManager, boolean isComFieldList) throws IOException {

        List<ColumnDefinition> columnDefinitions = new LinkedList<>();
        List<ResultSetRow> resultSetRows = new LinkedList<>();

        MyPacketChannel packetChannel = channelManager.getChanel();
        CapabilityFlags capabilityFlags = channelManager.getCapabilityFlags();


        byte[] resultBytes = packetChannel.readAll();

        //
        ResultPacket resultPacket = getSimpleResultPacket(resultBytes, capabilityFlags);
        if(resultPacket != null) {
            return resultPacket;
        }

        ByteArrayInputStreamReader reader = new ByteArrayInputStreamReader(resultBytes);
        reader.skip(4);
        long columnCount = reader.readNextLenEncInt();
        // MySQL 5.7.5版本开始可以通过指定CLIENT_DEPRECATE_EOF,使用Ok Packet 取代EOF Packet
        byte endPacketType =  capabilityFlags.isSupportsDeprecateEof() ?  (byte)NativeConstants.TYPE_ID_OK
                : (byte)NativeConstants.TYPE_ID_EOF;
        if (columnCount > 0) {
            // 解析列定义
            byte[] bytes = null;
            while ((bytes = packetChannel.readAll())[4] != endPacketType)
                if (capabilityFlags.isSupportsProtocol41())
                    columnDefinitions.add(new ColumnDefinition41(bytes, isComFieldList));
                else
                    columnDefinitions.add(new ColumnDefinition320(bytes, isComFieldList, capabilityFlags));
            // 解析值
            while ((bytes = packetChannel.readAll())[4] != endPacketType)
                resultSetRows.add(new ResultSetRow(bytes));

            ResultPacket result = getSimpleResultPacket(bytes, capabilityFlags);
            if(result != null) {
                result.setColumnDefinitionList(columnDefinitions);
                result.setResultSetRowList(resultSetRows);
                return result;
            }
        }

        throw new RuntimeException("解析结果包失败，未知类型的Packet");
    }


    /**
     *  https://dev.mysql.com/doc/internals/en/generic-response-packets.html
     * @param bytes
     * @param capabilityFlags
     * @return
     * @throws IOException
     */
    private static ResultPacket getSimpleResultPacket(byte[] bytes, CapabilityFlags capabilityFlags) throws IOException {

        // 前3字节为包长度
        int packetLen = ByteConvertUtil.getIntLn(Arrays.copyOfRange(bytes, 0, 3));

        // 第4字节为序列号，第5字节开始为才是包内容，包内容第一字节为类型
        byte header = bytes[4];

        // ERR Packet
        if (header == (byte)NativeConstants.TYPE_ID_ERROR) {
            return new ResultPacket("ERR", new ErrPacket(bytes, capabilityFlags));
        }
        // EOF Packet
        if (packetLen < 9 && header == (byte)NativeConstants.TYPE_ID_EOF) {
            return new ResultPacket("EOF", new EofPacket(bytes, capabilityFlags));
        }

        // OK Packet
        // TODO 官方文档不准确？发现有包长=7的OK Packet
       /*
          OK: header = 0 and length of packet > 7
          EOF: header = 0xfe and length of packet < 9
        */
        if (header == (byte)NativeConstants.TYPE_ID_OK) {
            return new ResultPacket("OK", new OkPacket(bytes, capabilityFlags));
        }

        return null;
    }


}
