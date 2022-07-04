package yanyu.xmz.recorder;

import com.github.shyiko.mysql.binlog.network.protocol.command.*;
import yanyu.xmz.recorder.mysql.channel.MyPacketChannel;
import yanyu.xmz.recorder.mysql.common.ResultParser;
import yanyu.xmz.recorder.mysql.common.ResultPacket;
import yanyu.xmz.recorder.mysql.protocol.CapabilityFlags;
import yanyu.xmz.recorder.mysql.channel.ChannelManager;
import yanyu.xmz.recorder.business.dao.util.PropertiesReaderUtil;

import java.io.IOException;

/**
 * @author xiaomingzhang
 * @date 2022/6/29
 */
public class CommandTest {


    private static final String hostname;

    private static final Integer port;

    private static final String username;

    private static final String password;

    static {
        hostname = PropertiesReaderUtil.get("mysql.listener.hostname");
        port = Integer.valueOf(PropertiesReaderUtil.get("mysql.listener.port"));
        username = PropertiesReaderUtil.get("mysql.listener.username");
        password = PropertiesReaderUtil.get("mysql.listener.password");
    }


    public static void main(String[] args) throws IOException {

        ChannelManager connectionChannel = new ChannelManager(hostname, port, username, password);

        try {
            // 执行Ping命令
            sendCommand(connectionChannel, new PingCommand());

            // 执行创建数据库命令
            sendCommand(connectionChannel, new QueryCommand("create database xmz"));

            // 执行删除数据库命令
            sendCommand(connectionChannel, new QueryCommand("drop database xmz"));

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            connectionChannel.close();
        }
    }

    private static void sendCommand(ChannelManager connectionChannel, Command command) throws IOException {
        MyPacketChannel localChannel = connectionChannel.getChanel();
        // 获取能力标记
        CapabilityFlags capabilityFlags = connectionChannel.getCapabilityFlags();
        // 执行查询命令
        localChannel.write(command);
        byte[] resultBytes = localChannel.readAll();
        // 解析结果
        ResultPacket resultPacket = ResultParser.parseResultPacket(resultBytes, capabilityFlags);
        System.out.println(resultPacket);
    }





}
