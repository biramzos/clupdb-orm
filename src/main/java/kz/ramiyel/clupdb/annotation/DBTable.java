package kz.ramiyel.clupdb.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface DBTable {
    String name();
    String comment() default "";
    String schema() default "";
    DBIndex[] indexes() default {};
}
