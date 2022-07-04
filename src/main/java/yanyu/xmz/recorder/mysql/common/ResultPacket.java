package yanyu.xmz.recorder.mysql.common;

import yanyu.xmz.recorder.mysql.protocol.CapabilityFlags;
import yanyu.xmz.recorder.mysql.protocol.packet.Packet;

/**
 * @author xiaomingzhang
 * @date 2022/7/2
 */
public class ResultPacket {

    private String code;

    private Packet packet;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    @Override
    public String toString() {
        return "ResultPacket{" +
                "code='" + code + '\'' +
                ", packet=" + packet +
                '}';
    }

}
