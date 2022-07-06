package yanyu.xmz.recorder.mysql.common.util;


/**
 * @author xiaomingzhang
 * @date 2022/7/2
 */
public class ByteConvertUtil {


    /**
     * 大端序(Big-Endian)字节数组转int
     * 方法来源
     * @see com.github.shyiko.mysql.binlog.io.ByteArrayInputStream#readInteger
     *说明，网络传输字节一般采用大端序
     * 例如
     * bytes数组={1, 2, 3}
     * 对应二进制序列 00000001 00000010 00000011
     * 相当于(2^16) + (2^9) + (2^1 + 2^0) = 66051
     * @param bytes
     * @return
     */
    public static int getIntBn(byte[] bytes) {
        if(bytes == null || bytes.length == 0) {
            new IllegalArgumentException("字符数组转换int失败, byte数组不能为空");
        }
        int result = 0;
        for (int i = 0; i < bytes.length; ++i) {
            result |= (bytes[(bytes.length - 1) - i] << (i << 3));
        }
        return result;
    }

    /**
     * 小端序Little-Endian字节数组转int
     * 和大端序转int一样，见
     * @see this#getIntBn
     * @param bytes
     * @return
     */
    public static int getIntLn(byte... bytes) {
        if(bytes == null || bytes.length == 0) {
            new IllegalArgumentException("字符数组转换int失败, byte数组不能为空");
        }
        int result = 0;
        for (int i = 0; i < bytes.length; ++i) {
            result |= (bytes[i] << (i << 3));
        }
        return result;
    }



    /**
     * 拼接多个byte数组
     * @param bytes
     * @return
     */
    public static byte[] bytesConcat(byte[]... bytes) {
        int len = 0;
        for (byte[] byteArr : bytes){
            len += byteArr.length;
        }

        int i = 0;
        byte[] byteResult = new byte[len];
        for (byte[] byteArr : bytes) {
            for (byte b : byteArr){
                byteResult[i++] = b;
            }
        }
        return byteResult;
    }

    /**
     * 反转数组
     * @see java.util.Collections#reverse
     * @param bytes
     */
    public static void reverse(byte[] bytes) {
        byte tmp;
        for(int i=0, mid=bytes.length>>1, j=bytes.length-1; i<mid; i++, j--){
            tmp = bytes[i];
            bytes[i] = bytes[j];
            bytes[j] = tmp;
        }
    }


    public static void main(String[] args) {

        // test getInt
        byte[] bytes = {1,2,3};
        System.out.println(getIntBn(bytes));

        // test bytesConcat
        byte[] bytes2 = {0,4,1};
        byte[] bytes3 = bytesConcat(bytes, bytes2);
        System.out.println(toString(bytes3));

        // test reverse
        byte[] bytes4 = {1,2,3,4,5};
        reverse(bytes4);

        System.out.println(toString(bytes4));

    }

    private static String toString(byte[] bytes) {
        String str = "";
        for (int i = 0; i < bytes.length; i++) {
            str += bytes[i];
            if(i !=  bytes.length - 1){
                str += ",";
            }
        }
        return str;
    }



}
