package yanyu.xmz.recorder.mysql.common;

import yanyu.xmz.recorder.mysql.protocol.packet.Packet;
import yanyu.xmz.recorder.mysql.protocol.packet.resultset.ColumnDefinition;
import yanyu.xmz.recorder.mysql.protocol.packet.resultset.ResultSetRow;

import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/7/2
 */
public class ResultPacket {

    /**
     * 结果包类型
     * OK、ERR、EOF
     */
    private String type;

    private Packet packet;

    private List<ColumnDefinition> columnDefinitionList;

    private List<ResultSetRow> resultSetRowList;

    public ResultPacket() {
    }

    public ResultPacket(String type, Packet packet) {
        this.type = type;
        this.packet = packet;
    }

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

    public List<ColumnDefinition> getColumnDefinitionList() {
        return columnDefinitionList;
    }

    public void setColumnDefinitionList(List<ColumnDefinition> columnDefinitionList) {
        this.columnDefinitionList = columnDefinitionList;
    }

    public List<ResultSetRow> getResultSetRowList() {
        return resultSetRowList;
    }

    public void setResultSetRowList(List<ResultSetRow> resultSetRowList) {
        this.resultSetRowList = resultSetRowList;
    }

    @Override
    public String toString() {
        return "ResultPacket{" +
                "type='" + type + '\'' +
                ", packet=" + packet +
                ", columnDefinitionList=" + columnDefinitionList +
                ", resultSetRowList=" + resultSetRowList +
                '}';
    }
}
