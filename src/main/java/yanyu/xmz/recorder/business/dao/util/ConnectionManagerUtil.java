package yanyu.xmz.recorder.business.dao.util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author xiaomingzhang
 * @date 2022/1/8
 */
public class ConnectionManagerUtil {

    private final static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    /**
     * 获取连接对象，并设置到当前线程ThreadLocal
     * @return
     */
    public static Connection getConnection() {
        Connection conn = connectionThreadLocal.get();
        if(conn == null) {
            conn = ConnectUtil.getConnection();
            connectionThreadLocal.set(conn);
        } else {
            try {
                if (conn.isClosed()) {
                    conn = ConnectUtil.getConnection();
                    connectionThreadLocal.set(conn);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return conn;
    }

    /**
     * 关闭连接
     */
    public static void closeConnection() {
        Connection conn = connectionThreadLocal.get();
        if (conn != null) {
            try {
                if(!conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        connectionThreadLocal.remove();
    }

}
