package bebetter.generate.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
public @interface GEntity {
    String value() default "";//中文名

    String[] userHideColumns() default "";//用户端隐藏字段
}
