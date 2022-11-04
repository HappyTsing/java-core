package coreII.CH08;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author happytsing
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface People {
    // 属性是value()时，使用注解时可以省略 属性名=
    String value();

    String Name() default "happytsing";
}
