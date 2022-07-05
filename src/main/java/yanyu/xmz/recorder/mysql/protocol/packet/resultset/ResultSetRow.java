package yanyu.xmz.recorder.mysql.protocol.packet.resultset;

import com.github.shyiko.mysql.binlog.io.ByteArrayInputStream;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/7/5
 */
public class ResultSetRow {

    private String[] values;

    public ResultSetRow(byte[] bytes) throws IOException {
        ByteArrayInputStream buffer = new ByteArrayInputStream(bytes);
        buffer.skip(4);
        List<String> values = new LinkedList<String>();
        while (buffer.available() > 0) {
            values.add(buffer.readLengthEncodedString());
        }
        this.values = values.toArray(new String[values.size()]);
    }

    public String[] getValues() {
        return values;
    }

    public String getValue(int index) {
        return values[index];
    }

    public int size() {
        return values.length;
    }

    @Override
    public String toString() {
        return "ResultSetRow{" +
                "values=" + Arrays.toString(values) +
                '}';
    }
}
