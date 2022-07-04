package yanyu.xmz.recorder.mysql.common;

import yanyu.xmz.recorder.mysql.protocol.packet.Packet;

/**
 * @author xiaomingzhang
 * @date 2022/7/2
 */
public class ResultPacket {

    private String type;

    private Packet packet;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
                "code='" + type + '\'' +
                ", packet=" + packet +
                '}';
    }

}
