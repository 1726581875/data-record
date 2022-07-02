package yanyu.xmz.recorder.business.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xiaomingzhang
 * @date 2022/6/20
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableField {

    /**
     * 列字段名
     * @return
     */
    String value() default "";

    /**
     * 字段类型
     * @return
     */
    String type() default "";

}
