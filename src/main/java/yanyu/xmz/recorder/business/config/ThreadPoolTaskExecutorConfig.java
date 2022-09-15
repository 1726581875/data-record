package yanyu.xmz.recorder.business.config;

import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xiaomingzhang
 * @date 2022/9/15
 */
@Configuration
public class ThreadPoolTaskExecutorConfig {


    @Bean
    public ThreadPoolTaskExecutor dataMigrationThreadPoolExecutor(TaskExecutorBuilder builder) {
        ThreadPoolTaskExecutor taskExecutor = builder.build();
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setThreadNamePrefix("dataMigration-");
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setCorePoolSize(1);
        return taskExecutor;
    }


}
