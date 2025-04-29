package kz.ramiyel.clupdb.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface DBTransaction {
    String name();
    String[] statements();
    String description() default "";
}
