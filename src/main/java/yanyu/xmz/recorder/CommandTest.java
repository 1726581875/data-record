package yanyu.xmz.recorder;

import com.github.shyiko.mysql.binlog.network.protocol.PacketChannel;
import com.github.shyiko.mysql.binlog.network.protocol.command.*;
import yanyu.xmz.recorder.mysql.channel.MyPacketChannel;
import yanyu.xmz.recorder.mysql.protocol.CapabilityFlags;
import yanyu.xmz.recorder.mysql.channel.ChannelManager;
import yanyu.xmz.recorder.mysql.protocol.packet.ErrPacket;
import yanyu.xmz.recorder.mysql.protocol.packet.OkPacket;
import yanyu.xmz.recorder.business.dao.util.PropertiesReaderUtil;
import yanyu.xmz.recorder.mysql.protocol.command.CreateDbCommand;

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
            MyPacketChannel localChannel = connectionChannel.getChanel();
            // 初始化功能标记
            CapabilityFlags capabilityFlags = connectionChannel.getCapabilityFlags();
            String capability = capabilityFlags.toString();
            System.out.println(capability);

            // 执行命令1
            localChannel.write(new CreateDbCommand("ddd"));
            byte[] read = localChannel.readAll();
/*            if(read[4] == (byte)0xFF) {
                ErrPacket errPacket = new ErrPacket(read,capabilityFlags);
                System.out.println(errPacket);
            } else {
                OkPacket okPacket = new OkPacket(read, capabilityFlags);
                System.out.println(okPacket);
            }*/

            // 执行命令2
            localChannel.write(new PingCommand());
            byte[] resultBytes = localChannel.readAll();
            if(resultBytes[4] == (byte)0xFF) {
                ErrPacket errPacket = new ErrPacket(resultBytes, capabilityFlags);
                System.out.println(errPacket);
            }else {
                OkPacket okPacket = new OkPacket(resultBytes, capabilityFlags);
                System.out.println(okPacket);
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            connectionChannel.close();
        }
    }




}
