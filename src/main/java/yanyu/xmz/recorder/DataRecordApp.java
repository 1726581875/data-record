package yanyu.xmz.recorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.dao.util.ConnectUtil;
import yanyu.xmz.recorder.business.entity.event.BinlogListenState;

/**
 * @author xiaomingzhang
 * @date 2022/9/14
 */
@RestController
@SpringBootApplication
public class DataRecordApp implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataRecordApp.class);

    @Value("${data-record.datasource.url:jdbc:mysql://localhost:3306/bin_log_record?characterEncoding=UTF-8&serverTimezone=GMT+8}")
    private String url;

    @Value("${data-record.datasource.username:root}")
    private String username;

    @Value("${data-record.datasource.password:root}")
    private String password;

    @Value("${data-record.datasource.driver:com.mysql.cj.jdbc.Driver}")
    private String driver;

    public static void main(String[] args) {
        SpringApplication.run(DataRecordApp.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        log.info("初始化local数据源 >> ");
        ConnectUtil.Config config = new ConnectUtil.Config(url, username, password, driver);
        log.info("localDataSource={}", config);
        ConnectUtil.setDataSource("local", config);
        ConnectUtil.setConfig(config);
        log.info("初始化local数据源结束 << ");

        BaseDAO.mysqlInstance().createTableIfNotExist(BinlogListenState.class);
        BaseDAO.mysqlInstance().exec("update binlog_listen_state set state = '"+ BinlogListenState.STATE_SERVER_RESTART +"' where state = '1'");

    }

}
