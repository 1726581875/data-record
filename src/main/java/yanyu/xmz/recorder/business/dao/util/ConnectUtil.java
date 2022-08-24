package yanyu.xmz.recorder.business.dao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xmz
 * 2020年9月12日
 * 数据库连接工具
 */
public class ConnectUtil {

    private static Config config;

    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String H2_DRIVER = "org.h2.Driver";
    /**
     * 达梦数据库连接驱动
     */
    public static final String DM_DRIVER = "dm.jdbc.driver.DmDriver";

    private static final Map<String, Config> dataSourceMap = new HashMap<>();

    static {
        Config local = new Config(PropertiesReaderUtil.get("mysql.url"),
                PropertiesReaderUtil.get("mysql.username"),
                PropertiesReaderUtil.get("mysql.password"),
                PropertiesReaderUtil.get("mysql.driver"));

        Config monitor = new Config(PropertiesReaderUtil.get("mysql.url"),
                PropertiesReaderUtil.get("mysql.username"),
                PropertiesReaderUtil.get("mysql.password"),
                PropertiesReaderUtil.get("mysql.driver"));
        config = local;
        dataSourceMap.put("local", local);
        dataSourceMap.put("monitor", monitor);
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    public static Connection getConnection() {
        return getConnection(config);
    }


    public static Connection getConnection(Config config){
        try {
            Class.forName(config.getDriver());
            Connection conn = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
            return conn;
        } catch (Exception e) {
            throw new RuntimeException("获取数据库连接失败", e);
        }
    }

    public synchronized static void setConfig(Config connConfig){
        config = connConfig;
    }

    public static Config getConfig(){
       return config;
    }


    public final static class Config {

        private final String URL_TEMPLATE = "jdbc:mysql://%s:%s/%s?characterEncoding=UTF-8&serverTimezone=GMT%%2B8";

        private String url;
        private String username;
        private String password;
        private String driver;

        public Config(String hostName, int port, String databaseName, String username, String password) {
            this.url = String.format(URL_TEMPLATE, hostName, port, databaseName);
            this.username = username;
            this.password = password;
            this.driver = MYSQL_DRIVER;
        }

        public Config(String url, String username, String password, String driver) {
            this.url = url;
            this.username = username;
            this.password = password;
            this.driver = driver;
        }

        public String getUrl() {
            return url;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getDriver() {
            return driver;
        }
    }



}
