package club.ensoul.framework.chatgtp.annotation;

import java.lang.annotation.*;

/**
 * 标识该类为 Beta
 *
 * @author wy_peng_chen6
 */
@Documented
@Target({ElementType.TYPE_USE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Limited {

    String value() default "";

}
