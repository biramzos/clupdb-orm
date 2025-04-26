package kz.ramiyel.clupdb.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
public @interface DBColumn {
    String name();
    String type();
    String comment() default "";
    boolean unique() default false;
    boolean primaryKey() default false;
    boolean autoIncrement() default false;
    boolean notNull() default false;
    String defaultValue() default "";
    boolean foreignKey() default false;
    String referencedTable() default "";
    String referencedColumn() default "";
}
