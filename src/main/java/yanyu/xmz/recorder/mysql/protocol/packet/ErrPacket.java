package yanyu.xmz.recorder.mysql.protocol.packet;

import yanyu.xmz.recorder.mysql.protocol.CapabilityFlags;

import java.io.IOException;

/**
 * @author xiaomingzhang
 * @date 2022/6/30
 * 错误包
 * https://dev.mysql.com/doc/internals/en/packet-ERR_Packet.html
 */
public class ErrPacket extends Packet {
    /**
     * [ff] header of the ERR packet，错误包开头为ff，占一字节
     */
    private byte header;
    /**
     * 错误码，占2字节
     */
    private int errorCode;
    /**
     * SQL 状态
     */
    private String sqlState;
    /**
     * 错误消息
     */
    private String errorMessage;

    public ErrPacket(byte[] bytes, CapabilityFlags capabilityFlags) throws IOException {
        super(bytes);
        this.header = (byte) reader.readInteger(1);
        this.errorCode = reader.readInteger(2);
        if (capabilityFlags.isSupportsProtocol41()) {
            // marker of the SQL State
            reader.skip(1);
            this.sqlState = reader.readString(5);
        }
        this.errorMessage = reader.readString(reader.available());
    }

    public byte getHeader() {
        return header;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getSqlState() {
        return sqlState;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "ErrPacket{" +
                "header=" + header +
                ", errorCode=" + errorCode +
                ", sqlState='" + sqlState + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", length=" + length +
                ", sequenceId=" + sequenceId +
                '}';
    }
}
