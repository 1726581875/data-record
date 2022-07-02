package yanyu.xmz.recorder.mysql.channel;

import com.github.shyiko.mysql.binlog.io.ByteArrayInputStream;
import com.github.shyiko.mysql.binlog.network.protocol.PacketChannel;
import yanyu.xmz.recorder.mysql.common.ByteConvertUtil;

import java.io.IOException;
import java.net.Socket;

/**
 * @author xiaomingzhang
 * @date 2022/7/2
 */
public class MyPacketChannel extends PacketChannel {

    public MyPacketChannel(String hostname, int port) throws IOException {
        super(hostname, port);
    }

    public MyPacketChannel(Socket socket) throws IOException {
        super(socket);
    }

    /**
     * 该方法获取包返回所有字节序列
     * PacketChannel类的read方法，会取走数据包长度，导致后续解析结果包获取不到包长
     * @return
     * @throws IOException
     */
    public byte[] readAll() throws IOException {
        ByteArrayInputStream inputStream = super.getInputStream();
        // 字节序由网络传输大端序按顺序存放于数组则变成了小端序
        byte[] lenBytes = inputStream.read(3);
        // 数据包长度
        int length = ByteConvertUtil.getIntLn(lenBytes);
        return ByteConvertUtil.bytesConcat(lenBytes, inputStream.read(1), inputStream.read(length));
    }
}
