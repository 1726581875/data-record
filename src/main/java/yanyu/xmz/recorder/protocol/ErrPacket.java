package yanyu.xmz.recorder.protocol;

import com.github.shyiko.mysql.binlog.io.ByteArrayInputStream;

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

    public ErrPacket(byte[] bytes) throws IOException {
        ByteArrayInputStream buffer = new ByteArrayInputStream(bytes);
        this.header = (byte) buffer.readInteger(1);
        this.errorCode = buffer.readInteger(2);
        if (CapabilityFlags.isSupportsProtocol41()) {
            // marker of the SQL State
            buffer.skip(1);
            this.sqlState = buffer.readString(5);
        }
        this.errorMessage = buffer.readString(buffer.available());
    }

    @Override
    public String toString() {
        return "ErrPacket{" +
                "header=" + header +
                ", errorCode=" + errorCode +
                ", sqlState='" + sqlState + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
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
}
