package yanyu.xmz.recorder.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xiamingzhang
 * @data 2022/06/13
 * 时间类型由数据库自动插入
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateAuto {
    /**
     * 可选择值
     * create : 创建时
     * update : 更新时
     *
     * @return
     */
    String value() default "create";
}
