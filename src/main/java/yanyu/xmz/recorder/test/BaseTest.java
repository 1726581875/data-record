package yanyu.xmz.recorder.test;

import yanyu.xmz.recorder.business.dao.util.PropertiesReaderUtil;

/**
 * @author xiaomingzhang
 * @date 2022/7/5
 */
public class BaseTest {

    protected static final String hostname;

    protected static final Integer port;

    protected static final String username;

    protected static final String password;

    static {
        hostname = PropertiesReaderUtil.get("mysql.monitor.hostname");
        port = Integer.valueOf(PropertiesReaderUtil.get("mysql.monitor.port"));
        username = PropertiesReaderUtil.get("mysql.monitor.username");
        password = PropertiesReaderUtil.get("mysql.monitor.password");
    }

}
