package kz.ramiyel.clupdb.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface DBProcedure {
    String name();
    String[] parameters() default {};
    String body();
    String language() default "plpgsql";
    String description() default "";
}
