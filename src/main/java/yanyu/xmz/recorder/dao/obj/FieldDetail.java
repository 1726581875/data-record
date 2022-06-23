package yanyu.xmz.recorder.dao.obj;

/**
 * @author xiaomingzhang
 * @date 2022/1/7
 */
public class FieldDetail {

    private String fieldName;
    /**
     * 插入语句占位符参数下标
     */
    private int paramIndex;
    /**
     * 对应java.sql.types的类型值
     */
    private int sqlType;

    private Class<?> fieldClass;

    public FieldDetail() {

    }

    public FieldDetail(String fieldName, int paramIndex, int sqlType, Class<?> fieldClass) {
        this.fieldName = fieldName;
        this.paramIndex = paramIndex;
        this.sqlType = sqlType;
        this.fieldClass = fieldClass;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getParamIndex() {
        return paramIndex;
    }

    public void setParamIndex(int paramIndex) {
        this.paramIndex = paramIndex;
    }

    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }


    public Class<?> getFieldClass() {
        return fieldClass;
    }

    public void setFieldClass(Class<?> fieldClass) {
        this.fieldClass = fieldClass;
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "fieldName='" + fieldName + '\'' +
                ", paramIndex='" + paramIndex + '\'' +
                ", sqlType=" + sqlType +
                ", fieldClass=" + fieldClass +
                '}';
    }
}
