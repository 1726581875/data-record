package yanyu.xmz.recorder.mysql.channel;

import com.github.shyiko.mysql.binlog.io.ByteArrayInputStream;
import com.github.shyiko.mysql.binlog.network.AuthenticationException;
import com.github.shyiko.mysql.binlog.network.ServerException;
import com.github.shyiko.mysql.binlog.network.protocol.ErrorPacket;
import com.github.shyiko.mysql.binlog.network.protocol.GreetingPacket;
import com.github.shyiko.mysql.binlog.network.protocol.PacketChannel;
import com.github.shyiko.mysql.binlog.network.protocol.command.AuthenticateCommand;
import com.github.shyiko.mysql.binlog.network.protocol.command.AuthenticateNativePasswordCommand;
import com.github.shyiko.mysql.binlog.network.protocol.command.Command;
import yanyu.xmz.recorder.mysql.protocol.CapabilityFlags;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

/**
 * @author xiaomingzhang
 * @date 2022/7/2
 * 封装连接通道
 */
public class ChannelManager {

    private MyPacketChannel packetChannel;

    private String hostname;

    private Integer port;

    private String username;

    private String password;

    private String database;

    private Long connectTimeout;

    /**
     * 客户端/服务端 使用的能力
     */
    private CapabilityFlags capabilityFlags;

    public ChannelManager(String hostname, Integer port, String username, String password) {
        this(hostname, port, username, password, null);
    }


    public ChannelManager(String hostname, Integer port, String username, String password, String database) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }


    public MyPacketChannel getChanel() throws IOException {

        if(packetChannel == null) {
            packetChannel = openConnectChanel();
        }

        if(!packetChannel.isOpen()){
            packetChannel = openConnectChanel();
        }

        return packetChannel;
    }


    /**
     * 握手方法参考源码
     * @see com.github.shyiko.mysql.binlog.BinaryLogClient#openChannelToBinaryLogStream
     *
     * https://dev.mysql.com/doc/internals/en/plain-handshake.html(普通握手)
     * @return
     * @throws IOException
     */
    private MyPacketChannel openConnectChanel() throws IOException {
        // 1、和mysql建立连接，会首先收到服务器发来的初始化握手包
        MyPacketChannel localChannel = openChannel(connectTimeout == null ? 1000 * 60 : connectTimeout,  hostname, port);
        GreetingPacket greetingPacket = receiveGreeting(localChannel);
        // 2、握手响应，携带并授权信息，接收到服务发送回来的ok包就表示验证成功
        // Protocol::HandshakeResponse41握手响应包具体内容见 https://dev.mysql.com/doc/internals/en/connection-phase-packets.html
        authenticate(localChannel, greetingPacket);

        // todo 需要确认
        capabilityFlags = new CapabilityFlags(greetingPacket.getServerCapabilities());
        return localChannel;
    }


    private MyPacketChannel openChannel(final long connectTimeout, final String hostname, final int port) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(hostname, port), (int) connectTimeout);
        return new MyPacketChannel(socket);
    }

    private GreetingPacket receiveGreeting(final PacketChannel channel) throws IOException {
        byte[] initialHandshakePacket = channel.read();
        if (initialHandshakePacket[0] == (byte) 0xFF /* error */) {
            byte[] bytes = Arrays.copyOfRange(initialHandshakePacket, 1, initialHandshakePacket.length);
            ErrorPacket errorPacket = new ErrorPacket(bytes);
            throw new ServerException(errorPacket.getErrorMessage(), errorPacket.getErrorCode(),
                    errorPacket.getSqlState());
        }
        return new GreetingPacket(initialHandshakePacket);
    }


    private void authenticate(final PacketChannel channel, GreetingPacket greetingPacket) throws IOException {
        int collation = greetingPacket.getServerCollation();
        int packetNumber = 1;

        boolean usingSSLSocket = false;

        AuthenticateCommand authenticateCommand = new AuthenticateCommand(database, username, password,
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


    private void switchAuthentication(final PacketChannel channel, byte[] authenticationResult, boolean usingSSLSocket)
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

    public CapabilityFlags getCapabilityFlags(){
        return capabilityFlags;
    }

    public void setConnectTimeout(Long connectTimeout){
        this.connectTimeout = connectTimeout;
    }


    public void close() throws IOException {
        if(packetChannel != null && packetChannel.isOpen()) {
            packetChannel.close();
        }
    }




}
