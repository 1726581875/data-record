package yanyu.xmz.recorder.mysql.protocol.io;

import com.github.shyiko.mysql.binlog.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author xiaomingzhang
 * @date 2022/7/1
 */
public class ByteArrayInputStreamReader extends ByteArrayInputStream {

    public ByteArrayInputStreamReader(InputStream inputStream) {
        super(inputStream);
    }

    public ByteArrayInputStreamReader(byte[] bytes) {
        super(bytes);
    }

    /**
     * https://dev.mysql.com/doc/internals/en/integer.html
     * Length-Encoded Integer Type
     * todo 该方法父类已经实现
     * @see com.github.shyiko.mysql.binlog.io.ByteArrayInputStream#readPackedNumber
     * @return
     * @throws IOException
     */
    public long readNextLenEncInt() throws IOException {
        int firstByte = super.readInteger(1);
        if (firstByte < 0xFB) {
            return firstByte;
        } else if (firstByte == 0xFC) {
            return super.readLong(2);
        } else if (firstByte == 0xFD) {
            return super.readLong(4);
        } else if (firstByte == 0xFE) {
            return super.readLong(8);
        }
        return 0;
    }


}
