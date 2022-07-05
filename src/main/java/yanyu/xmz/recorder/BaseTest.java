package yanyu.xmz.recorder;

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
        hostname = PropertiesReaderUtil.get("mysql.listener.hostname");
        port = Integer.valueOf(PropertiesReaderUtil.get("mysql.listener.port"));
        username = PropertiesReaderUtil.get("mysql.listener.username");
        password = PropertiesReaderUtil.get("mysql.listener.password");
    }

}
