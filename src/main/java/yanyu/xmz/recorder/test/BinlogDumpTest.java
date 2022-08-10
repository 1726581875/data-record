package yanyu.xmz.recorder.test;

import com.github.shyiko.mysql.binlog.network.protocol.command.DumpBinaryLogCommand;
import yanyu.xmz.recorder.mysql.channel.ChannelManager;
import yanyu.xmz.recorder.mysql.channel.MyPacketChannel;
import yanyu.xmz.recorder.mysql.common.ResultPacket;
import yanyu.xmz.recorder.mysql.common.ResultParser;

import java.io.IOException;

/**
 * @author xiaomingzhang
 * @date 2022/7/18
 */
public class BinlogDumpTest extends BaseTest {

    public static void main(String[] args) throws IOException {

        ChannelManager connectionChannel = new ChannelManager(hostname, port, username, password);

        try {
            MyPacketChannel chanel = connectionChannel.getChanel();
            chanel.write(new DumpBinaryLogCommand(2, "mysql-bin.000001",10));
            ResultPacket resultPacket = ResultParser.parseComQueryResult(connectionChannel, false);
            System.out.println(resultPacket);

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            connectionChannel.close();
        }
    }


}
