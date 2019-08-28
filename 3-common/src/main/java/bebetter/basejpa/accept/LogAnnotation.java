package bebetter.basejpa.accept;

import java.lang.annotation.*;

/**
 * 自定义注解类
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogAnnotation {

    String value() default "";
}
