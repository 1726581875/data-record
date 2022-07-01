package yanyu.xmz.recorder.protocol;

import yanyu.xmz.recorder.protocol.io.ByteArrayInputStreamReader;

import java.io.IOException;

/**
 * @author xiaomingzhang
 * @date 2022/6/30
 * https://dev.mysql.com/doc/internals/en/packet-OK_Packet.html
 */
public class OkPacket {

    private byte header;
    /**
     * 受影响的行
     * int<lenenc>
     */
    private long affectedRows;
    /**
     * 最后插入 ID
     * int<lenenc> 可变长int
     */
    private long lastInsertId;
    /**
     * 状态标志
     */
    private int statusFlags;

    /**
     * 警告数
     */
    private int warnings;

    /**
     * 状态信息
     */
    private String info;
    /**
     * 会话状态信息
     */
    private String sessionStateChanges;


    public OkPacket(byte[] bytes) throws IOException {
        ByteArrayInputStreamReader reader = new ByteArrayInputStreamReader(bytes);
        this.header = reader.read(1)[0];
        this.affectedRows = reader.readNextLenEncInt();
        this.lastInsertId = reader.readNextLenEncInt();

        if (CapabilityFlags.isSupportsProtocol41()) {
            this.statusFlags = reader.readInteger(2);
            this.warnings = reader.readInteger(2);
        } else if(CapabilityFlags.isSupportsTransactions()){
            this.statusFlags = reader.readInteger(2);
        }

        if(CapabilityFlags.isSupportsSessionTrack()) {
            long charL = reader.readNextLenEncInt();
            this.info = reader.readString((int) charL);
            //https://dev.mysql.com/doc/internals/en/status-flags.html#flag-SERVER_SESSION_STATE_CHANGED
            if((statusFlags & (byte)0x4000) != 0) {
                long charLen = reader.readNextLenEncInt();
                this.sessionStateChanges = reader.readString((int) charLen);
            }
        } else {
            this.info = reader.readString(reader.available());
        }

    }


    public byte getHeader() {
        return header;
    }

    public long getAffectedRows() {
        return affectedRows;
    }

    public long getLastInsertId() {
        return lastInsertId;
    }

    public int getStatusFlags() {
        return statusFlags;
    }

    public int getWarnings() {
        return warnings;
    }

    public String getInfo() {
        return info;
    }

    public String getSessionStateChanges() {
        return sessionStateChanges;
    }

    @Override
    public String toString() {
        return "OkPacket{" +
                "header=" + header +
                ", affectedRows=" + affectedRows +
                ", lastInsertId=" + lastInsertId +
                ", statusFlags=" + statusFlags +
                ", warnings=" + warnings +
                ", info='" + info + '\'' +
                ", sessionStateChanges='" + sessionStateChanges + '\'' +
                '}';
    }
}
