package yanyu.xmz.recorder.mysql.protocol;

/**
 * @author xiaomingzhang
 * @date 2022/6/30
 */
public class Packet {
    /**
     * 包长度, 3字节
     */
    private int length;
    /**
     * 序列号，1字节
     */
    private byte sequenceId;

}
