package yanyu.xmz.recorder.mysql.protocol.packet.resultset;

import yanyu.xmz.recorder.mysql.protocol.CapabilityFlags;
import yanyu.xmz.recorder.mysql.protocol.io.ByteArrayInputStreamReader;

import java.io.IOException;

/**
 * @author xiaomingzhang
 * @date 2022/7/5
 * https://dev.mysql.com/doc/internals/en/com-query-response.html#column-definition
 */
public class ColumnDefinition320 extends ColumnDefinition {

    private String table;

    private String name;

    private Number columnLengthLen;

    private int columnLength;

    private Number typeLen;

    private int type;

    /*
     *   if capabilities & CLIENT_LONG_FLAG {
       lenenc_int     [03] length of flags+decimals fields
       2              flags
       1              decimals
         } else {
       1              [02] length of flags+decimals fields
       1              flags
       1              decimals
         }
    * */
    /**
     * length of flags+decimals fields
     */
    private Number flagAndDecimalLen;

    private int flags;

    private int decimals;

    /* if command was COM_FIELD_LIST */
    /**
     * length of default-values
     */
    private int defValLen;

    private String defaultValues;

    public ColumnDefinition320(byte[] bytes, boolean isComFieldList, CapabilityFlags capabilityFlags) throws IOException {
        ByteArrayInputStreamReader reader = new ByteArrayInputStreamReader(bytes);
        reader.skip(4);
        this.table = reader.readLengthEncodedString();
        this.name = reader.readLengthEncodedString();
        this.columnLengthLen = reader.readPackedNumber();
        this.columnLength = reader.readInteger(3);
        this.typeLen = reader.readPackedNumber();
        this.type = reader.readInteger(1);

        if(capabilityFlags.isSupportsLongFlag()){
            this.flagAndDecimalLen = reader.readPackedNumber();
            this.flags = reader.readInteger(2);
            this.decimals = reader.readInteger(1);
        }else {
            this.flagAndDecimalLen = reader.readPackedNumber();
            this.flags = reader.readInteger(1);
            this.decimals = reader.readInteger(1);
        }


        if(isComFieldList){
            this.defValLen = reader.readPackedInteger();
            this.defaultValues = reader.readString(this.defValLen);
        }

    }

    public String getTable() {
        return table;
    }

    public String getName() {
        return name;
    }

    public Number getColumnLengthLen() {
        return columnLengthLen;
    }

    public int getColumnLength() {
        return columnLength;
    }

    public Number getTypeLen() {
        return typeLen;
    }

    public int getType() {
        return type;
    }

    public Number getFlagAndDecimalLen() {
        return flagAndDecimalLen;
    }

    public int getFlags() {
        return flags;
    }

    public int getDecimals() {
        return decimals;
    }

    public int getDefValLen() {
        return defValLen;
    }

    public String getDefaultValues() {
        return defaultValues;
    }

    @Override
    public String toString() {
        return "ColumnDefinition320{" +
                "table='" + table + '\'' +
                ", name='" + name + '\'' +
                ", columnLengthLen=" + columnLengthLen +
                ", columnLength=" + columnLength +
                ", typeLen=" + typeLen +
                ", type=" + type +
                ", flagAndDecimalLen=" + flagAndDecimalLen +
                ", flags=" + flags +
                ", decimals=" + decimals +
                ", defValLen=" + defValLen +
                ", defaultValues='" + defaultValues + '\'' +
                '}';
    }
}
