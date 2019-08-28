package bebetter.generate.annotation;

import bebetter.generate.enums.EnumInputType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD})
@Retention(RUNTIME)
public @interface GColumn {
    String value() default "";//中文名

    EnumInputType type() default EnumInputType.none;//界面input框类型

    /**
     * 例如 [ rule_require('邮箱'), rule_email() ]
     * rule_require('邮箱')
     * rule_email()
     * rule_length(6,20)
     * rule_max_min(0,1000)
     */
    String[] rules() default "";

    boolean hideUser() default false;
}
