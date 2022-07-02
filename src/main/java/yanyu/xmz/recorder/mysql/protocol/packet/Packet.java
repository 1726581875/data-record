package yanyu.xmz.recorder.mysql.protocol.packet;

import yanyu.xmz.recorder.mysql.protocol.io.ByteArrayInputStreamReader;

import java.io.IOException;

/**
 * @author xiaomingzhang
 * @date 2022/6/30
 */
public class Packet {
    /**
     * 包长度, 3字节
     */
    protected int length;
    /**
     * 序列号，1字节
     */
    protected byte sequenceId;

    protected ByteArrayInputStreamReader reader;

    public Packet(byte[] bytes) throws IOException {
        reader = new ByteArrayInputStreamReader(bytes);
        this.length = reader.readInteger(3);
        this.sequenceId = (byte) reader.readInteger(1);
    }

    public int getLength() {
        return length;
    }

    public byte getSequenceId() {
        return sequenceId;
    }

    public ByteArrayInputStreamReader getReader() {
        return reader;
    }
}
