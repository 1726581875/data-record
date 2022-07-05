package yanyu.xmz.recorder;

import com.github.shyiko.mysql.binlog.network.protocol.command.QueryCommand;
import yanyu.xmz.recorder.mysql.channel.ChannelManager;
import yanyu.xmz.recorder.mysql.channel.MyPacketChannel;
import yanyu.xmz.recorder.mysql.common.ResultPacket;
import yanyu.xmz.recorder.mysql.common.ResultParser;

import java.io.IOException;

/**
 * @author xiaomingzhang
 * @date 2022/7/5
 */
public class QueryCommandTest extends BaseTest {



    public static void main(String[] args) throws IOException {

        ChannelManager connectionChannel = new ChannelManager(hostname, port, username, password);

        try {
            MyPacketChannel chanel = connectionChannel.getChanel();
            chanel.write(new QueryCommand("show databases"));
            ResultPacket resultPacket = ResultParser.parseComQueryResult(connectionChannel, false);
            System.out.println(resultPacket);

            chanel.write(new QueryCommand("create database xxx_xmz"));
            ResultPacket resultPacket1 = ResultParser.parseComQueryResult(connectionChannel, false);
            System.out.println(resultPacket1);

            chanel.write(new QueryCommand("drop database xxx_xmz"));
            ResultPacket resultPacket2 = ResultParser.parseComQueryResult(connectionChannel, false);
            System.out.println(resultPacket2);

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            connectionChannel.close();
        }
    }






}
