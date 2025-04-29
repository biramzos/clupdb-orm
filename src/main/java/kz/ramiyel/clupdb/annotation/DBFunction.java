package kz.ramiyel.clupdb.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface DBFunction {
    String name();
    String[] parameters() default {};
    String returnType() default "void";
    String body();
    String language() default "plpgsql";
    String description() default "";
}
