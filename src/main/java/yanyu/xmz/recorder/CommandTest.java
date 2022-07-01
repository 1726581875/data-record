package yanyu.xmz.recorder;

import com.github.shyiko.mysql.binlog.io.ByteArrayInputStream;
import com.github.shyiko.mysql.binlog.network.*;
import com.github.shyiko.mysql.binlog.network.protocol.ErrorPacket;
import com.github.shyiko.mysql.binlog.network.protocol.GreetingPacket;
import com.github.shyiko.mysql.binlog.network.protocol.PacketChannel;
import com.github.shyiko.mysql.binlog.network.protocol.command.*;
import yanyu.xmz.recorder.protocol.CapabilityFlags;
import yanyu.xmz.recorder.protocol.ErrPacket;
import yanyu.xmz.recorder.protocol.OkPacket;
import yanyu.xmz.recorder.dao.util.PropertiesReaderUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

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
        PacketChannel localChannel = openChannel(1000 * 60, hostname, port);

        try {
            GreetingPacket greetingPacket = receiveGreeting(localChannel);
            authenticate(localChannel, greetingPacket);
            // 初始化功能标记
            CapabilityFlags.init(greetingPacket.getServerCapabilities());
            String s = CapabilityFlags.supportsToString();
            System.out.println(s);
/*            localChannel.write(new CreateDbCommand("ddd"));

            byte[] read = localChannel.read();

            if(read[0] == (byte)0xFF) {
                ErrPacket errPacket = new ErrPacket(read);
                System.out.println(errPacket);
            }else {
                OkPacket okPacket = new OkPacket(read);
                System.out.println(okPacket);
            }*/

            localChannel.write(new PingCommand());
            byte[] resultBytes = localChannel.read();
            if(resultBytes[0] == (byte)0xFF) {
                ErrPacket errPacket = new ErrPacket(resultBytes);
                System.out.println(errPacket);
            }else {
                OkPacket okPacket = new OkPacket(resultBytes);
                System.out.println(okPacket);
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            localChannel.close();
        }
    }


    private static PacketChannel openChannel(final long connectTimeout, final String hostname, final int port) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(hostname, port), (int) connectTimeout);
        return new PacketChannel(socket);
    }

    private static GreetingPacket receiveGreeting(final PacketChannel channel) throws IOException {
        byte[] initialHandshakePacket = channel.read();
        if (initialHandshakePacket[0] == (byte) 0xFF /* error */) {
            byte[] bytes = Arrays.copyOfRange(initialHandshakePacket, 1, initialHandshakePacket.length);
            ErrorPacket errorPacket = new ErrorPacket(bytes);
            throw new ServerException(errorPacket.getErrorMessage(), errorPacket.getErrorCode(),
                    errorPacket.getSqlState());
        }
        return new GreetingPacket(initialHandshakePacket);
    }


    private static void authenticate(final PacketChannel channel, GreetingPacket greetingPacket) throws IOException {
        int collation = greetingPacket.getServerCollation();
        int packetNumber = 1;

        boolean usingSSLSocket = false;

        AuthenticateCommand authenticateCommand = new AuthenticateCommand(null, username, password,
                greetingPacket.getScramble());
        authenticateCommand.setCollation(collation);
        channel.write(authenticateCommand, packetNumber);
        byte[] authenticationResult = channel.read();
        if (authenticationResult[0] != (byte) 0x00 /* ok */) {
            if (authenticationResult[0] == (byte) 0xFF /* error */) {
                byte[] bytes = Arrays.copyOfRange(authenticationResult, 1, authenticationResult.length);
                ErrorPacket errorPacket = new ErrorPacket(bytes);
                throw new AuthenticationException(errorPacket.getErrorMessage(), errorPacket.getErrorCode(),
                        errorPacket.getSqlState());
            } else if (authenticationResult[0] == (byte) 0xFE) {
                switchAuthentication(channel, authenticationResult, usingSSLSocket);
            } else {
                throw new AuthenticationException("Unexpected authentication result (" + authenticationResult[0] + ")");
            }
        }
    }


    private static void switchAuthentication(final PacketChannel channel, byte[] authenticationResult, boolean usingSSLSocket)
            throws IOException {
        /*
            Azure-MySQL likes to tell us to switch authentication methods, even though
            we haven't advertised that we support any.  It uses this for some-odd
            reason to send the real password scramble.
        */
        ByteArrayInputStream buffer = new ByteArrayInputStream(authenticationResult);
        //noinspection ResultOfMethodCallIgnored
        buffer.read(1);

        String authName = buffer.readZeroTerminatedString();
        if ("mysql_native_password".equals(authName)) {
            String scramble = buffer.readZeroTerminatedString();

            Command switchCommand = new AuthenticateNativePasswordCommand(scramble, password);
            channel.write(switchCommand, (usingSSLSocket ? 4 : 3));
            byte[] authResult = channel.read();

            if (authResult[0] != (byte) 0x00) {
                byte[] bytes = Arrays.copyOfRange(authResult, 1, authResult.length);
                ErrorPacket errorPacket = new ErrorPacket(bytes);
                throw new AuthenticationException(errorPacket.getErrorMessage(), errorPacket.getErrorCode(),
                        errorPacket.getSqlState());
            }
        } else {
            throw new AuthenticationException("Unsupported authentication type: " + authName);
        }
    }


}
