package yanyu.xmz.recorder.mysql.protocol.packet;

import yanyu.xmz.recorder.mysql.protocol.CapabilityFlags;

import java.io.IOException;

/**
 * @author xiaomingzhang
 * @date 2022/7/4
 * 在5.7.5版本之后已经不推荐使用，OK Packet可以取代EOF Packet。
 * 并且可以通过CLIENT_DEPRECATE_EOF能力标识指定，让服务端使用OK Packet代替EOF Packet
 * https://dev.mysql.com/doc/internals/en/packet-OK_Packet.html
 */
public class EofPacket extends Packet {

    private byte header;

    private int warnings;

    private int statusFlags;

    public EofPacket(byte[] bytes, CapabilityFlags capabilityFlags) throws IOException {
        super(bytes);
        header = (byte) reader.readInteger(1);
        if(capabilityFlags.isSupportsProtocol41()) {
            warnings = reader.readInteger(2);
            statusFlags = reader.readInteger(2);
        }

    }


    public byte getHeader() {
        return header;
    }

    public int getWarnings() {
        return warnings;
    }

    public int getStatusFlags() {
        return statusFlags;
    }

    @Override
    public String toString() {
        return "EofPacket{" +
                "header=" + header +
                ", warnings=" + warnings +
                ", statusFlags=" + statusFlags +
                ", length=" + length +
                ", sequenceId=" + sequenceId +
                '}';
    }
}
