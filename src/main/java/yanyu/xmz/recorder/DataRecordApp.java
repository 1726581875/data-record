package yanyu.xmz.recorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaomingzhang
 * @date 2022/9/14
 */
@RestController
@SpringBootApplication
public class DataRecordApp {

    public static void main(String[] args) {
        SpringApplication.run(DataRecordApp.class, args);
    }

}
