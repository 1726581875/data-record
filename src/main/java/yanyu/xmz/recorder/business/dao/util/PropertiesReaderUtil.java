package yanyu.xmz.recorder.business.dao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author xiaomingzhang
 * @date 2021/8/30
 * 读取properties文件
 */
public class PropertiesReaderUtil {

    private static final Logger log = LoggerFactory.getLogger(PropertiesReaderUtil.class);

    private static final Properties properties = new Properties();

    static {
        Resource resource = new ClassPathResource("config.properties");
        try(InputStream inputStream = resource.getInputStream()) {
            properties.load(inputStream);
        } catch (IOException e) {
            log.error("加载配置文件config.properties发生异常", e);
        }
    }


    public static String get(String key){
        return properties.getProperty(key);
    }


}
