package kz.ramiyel.clupdb.util;

import kz.ramiyel.clupdb.annotation.DBColumn;
import kz.ramiyel.clupdb.annotation.DBTable;
import kz.ramiyel.clupdb.exception.ColumnNotFoundException;
import kz.ramiyel.clupdb.exception.TableNotFoundException;
import kz.ramiyel.clupdb.model.QueryColumn;
import kz.ramiyel.clupdb.model.QueryTable;

import java.lang.reflect.Field;

public class TableUtil {

    public static QueryTable parseTable(Class<?> clazz) {
        if (clazz.isAnnotationPresent(DBTable.class)) {
            DBTable table = clazz.getAnnotation(DBTable.class);
            return new QueryTable(table.schema(), table.name());
        }
        throw new TableNotFoundException();
    }

    public static QueryColumn parseColumn(Class<?> clazz, String variable) throws NoSuchFieldException {
        if (clazz.isAnnotationPresent(DBTable.class)) {
            QueryTable table = parseTable(clazz);
            return parseColumn(clazz, table, variable);
        }
        throw new TableNotFoundException();
    }

    public static QueryColumn parseColumn(Class<?> clazz, QueryTable table, String variable) {
        try {
            Field field = clazz.getDeclaredField(variable);
            if (field.isAnnotationPresent(DBColumn.class)) {
                DBColumn column = field.getAnnotation(DBColumn.class);
                return new QueryColumn(table, column.name());
            } else {
                throw new ColumnNotFoundException();
            }
        } catch (Exception e) {
            throw new ColumnNotFoundException();
        }
    }

}
