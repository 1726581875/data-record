package yanyu.xmz.recorder.mysql.event;

/**
 * @author xiaomingzhang
 * @date 2022/7/18
 * https://dev.mysql.com/doc/internals/en/event-structure.html
 *
 * v4 事件结构
 * +=====================================+
 * | event  | timestamp         0 : 4    |
 * | header +----------------------------+
 * |        | type_code         4 : 1    |
 * |        +----------------------------+
 * |        | server_id         5 : 4    |
 * |        +----------------------------+
 * |        | event_length      9 : 4    |
 * |        +----------------------------+
 * |        | next_position    13 : 4    |
 * |        +----------------------------+
 * |        | flags            17 : 2    |
 * |        +----------------------------+
 * |        | extra_headers    19 : x-19 |
 * +=====================================+
 * | event  | fixed part        x : y    |
 * | data   +----------------------------+
 * |        | variable part              |
 * +=====================================+
 */
public class EventHeaderV4 {

    private int timestamp;

    private int typeCode;

    private int serverId;

    private int eventLength;

    private int nextPosition;

    private int flags;

    private byte[] extraHeaders;




}
