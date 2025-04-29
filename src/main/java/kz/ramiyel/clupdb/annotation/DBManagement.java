package kz.ramiyel.clupdb.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DBManagement {
    DBFunction[] functions() default {};
    DBTransaction[] transactions() default {};
    DBProcedure[] procedures() default {};
}
