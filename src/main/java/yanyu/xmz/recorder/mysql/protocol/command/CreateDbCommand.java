package yanyu.xmz.recorder.mysql.protocol.command;

import com.github.shyiko.mysql.binlog.io.ByteArrayOutputStream;
import com.github.shyiko.mysql.binlog.network.protocol.command.Command;
import com.github.shyiko.mysql.binlog.network.protocol.command.CommandType;

import java.io.IOException;

/**
 * @author xiaomingzhang
 * @date 2022/6/29
 */
public class CreateDbCommand implements Command {

    private String schemaName;

    public CreateDbCommand(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        buffer.writeInteger(CommandType.CREATE_DB.ordinal(), 1);
        buffer.writeString(this.schemaName);
        return buffer.toByteArray();
    }


}
