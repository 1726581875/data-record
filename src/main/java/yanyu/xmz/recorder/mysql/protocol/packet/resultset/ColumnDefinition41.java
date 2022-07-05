package yanyu.xmz.recorder.mysql.protocol.packet.resultset;

import yanyu.xmz.recorder.mysql.protocol.io.ByteArrayInputStreamReader;

import java.io.IOException;

/**
 * @author xiaomingzhang
 * @date 2022/7/5
 * Protocol::ColumnDefinition41
 * https://dev.mysql.com/doc/internals/en/com-query-response.html#column-definition
 */
public class ColumnDefinition41 extends ColumnDefinition {

    /**
     * catalog (always "def")
     */
    private String catalog;
    /**
     *  schema-name
     */
    private String schema;
    /**
     *  virtual table-name
     */
    private String table;
    /**
     * physical table-name
     */
    private String orgTable;
    /**
     * virtual column name
     */
    private String name;
    /**
     * physical column name
     */
    private String orgName;

    /**
     * length of fixed-length fields [0c]
     */
    private Number nextLength;
    /**
     * 2 byte
     *  is the column character set and is defined in Protocol::CharacterSet.
     */
    private int characterSet;
    /**
     * 4 byte
     *  maximum length of the field
     */
    private int columnLength;
    /**
     * 1 byte
     * type of the column as defined in Column Type
     */
    private int type;
    /**
     * 2 byte
     */
    private int flags;
    /**
     * 1 byte
     *  max shown decimal digits
     * 0x00 for integers and static strings
     *
     * 0x1f for dynamic strings, double, float
     *
     * 0x00 to 0x51 for decimals
     */
    private int decimals;
    /**
     * 2 byte filler [00] [00]
     */
    private int filler;


    /* if command was COM_FIELD_LIST */
    /**
     * length of default-values
     */
    private int defValLen;

    private String defaultValues;

    /**
     *
     * @param bytes
     * @param isComFieldList 查询命令是否是 COM_FIELD_LIST
     */
    public ColumnDefinition41(byte[] bytes, boolean isComFieldList) throws IOException {
        ByteArrayInputStreamReader reader = new ByteArrayInputStreamReader(bytes);
        reader.skip(4);
        this.catalog = reader.readLengthEncodedString();
        this.schema = reader.readLengthEncodedString();
        this.table = reader.readLengthEncodedString();
        this.orgTable = reader.readLengthEncodedString();
        this.name = reader.readLengthEncodedString();
        this.orgName = reader.readLengthEncodedString();
        this.nextLength = reader.readPackedNumber();
        this.characterSet = reader.readInteger(2);
        this.columnLength = reader.readInteger(4);
        this.type = reader.readInteger(1);
        this.flags = reader.readInteger(2);
        this.decimals = reader.readInteger(1);
        this.filler = reader.readInteger(2);
        if(isComFieldList){
            this.defValLen = reader.readPackedInteger();
            this.defaultValues = reader.readString(this.defValLen);
        }

    }

    public String getCatalog() {
        return catalog;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public String getOrgTable() {
        return orgTable;
    }

    public String getName() {
        return name;
    }

    public String getOrgName() {
        return orgName;
    }

    public Number getNextLength() {
        return nextLength;
    }

    public int getCharacterSet() {
        return characterSet;
    }

    public int getColumnLength() {
        return columnLength;
    }

    public int getType() {
        return type;
    }

    public int getFlags() {
        return flags;
    }

    public int getDecimals() {
        return decimals;
    }

    public int getFiller() {
        return filler;
    }

    public int getDefValLen() {
        return defValLen;
    }

    public String getDefaultValues() {
        return defaultValues;
    }

    @Override
    public String toString() {
        return "ColumnDefinition41{" +
                "catalog='" + catalog + '\'' +
                ", schema='" + schema + '\'' +
                ", table='" + table + '\'' +
                ", orgTable='" + orgTable + '\'' +
                ", name='" + name + '\'' +
                ", orgName='" + orgName + '\'' +
                ", nextLength=" + nextLength +
                ", characterSet=" + characterSet +
                ", columnLength=" + columnLength +
                ", type=" + type +
                ", flags=" + flags +
                ", decimals=" + decimals +
                ", filler=" + filler +
                ", defValLen=" + defValLen +
                ", defaultValues='" + defaultValues + '\'' +
                '}';
    }
}
