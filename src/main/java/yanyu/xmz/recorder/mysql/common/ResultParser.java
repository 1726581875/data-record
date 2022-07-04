package yanyu.xmz.recorder.mysql.common;

import com.mysql.cj.protocol.a.NativeConstants;
import yanyu.xmz.recorder.mysql.protocol.CapabilityFlags;
import yanyu.xmz.recorder.mysql.protocol.io.ByteArrayInputStreamReader;
import yanyu.xmz.recorder.mysql.protocol.packet.EofPacket;
import yanyu.xmz.recorder.mysql.protocol.packet.ErrPacket;
import yanyu.xmz.recorder.mysql.protocol.packet.OkPacket;

import java.io.IOException;

/**
 * @author xiaomingzhang
 * @date 2022/7/4
 */
public class ResultParser {


    /**
     * https://dev.mysql.com/doc/internals/en/generic-response-packets.html
     * @param resultBytes
     * @return
     * @throws IOException
     */
   public static ResultPacket parseResultPacket(byte[] resultBytes, CapabilityFlags capabilityFlags) throws IOException {
       ByteArrayInputStreamReader reader = new ByteArrayInputStreamReader(resultBytes);
       // 获取包长
       int packetLen = reader.readInteger(3);
       // 跳过1字节(序列号)
       reader.skip(1);
       // 有效数据包第一个字节
       short header = (short) reader.readInteger(1);

       ResultPacket result = new ResultPacket();

       // ERR Packet
       if(header == NativeConstants.TYPE_ID_ERROR) {
           ErrPacket errPacket = new ErrPacket(resultBytes, capabilityFlags);
           result.setPacket(errPacket);
           result.setCode("ERR");
           return result;
       }
       // EOF Packet
       if(packetLen < 9 && header == NativeConstants.TYPE_ID_EOF) {
           EofPacket eofPacket = new EofPacket(resultBytes, capabilityFlags);
           result.setPacket(eofPacket);
           result.setCode("EOF");
           return result;
       }

       // OK Packet
       // TODO 官方文档不准确？发现有包长=7的OK Packet
       /*
       OK: header = 0 and length of packet > 7
       EOF: header = 0xfe and length of packet < 9
        */
       if(header == NativeConstants.TYPE_ID_OK) {
           OkPacket okPacket = new OkPacket(resultBytes, capabilityFlags);
           result.setPacket(okPacket);
           result.setCode("OK");
           return result;
       }

       throw new RuntimeException("未知类型的Packet");

    }

}
