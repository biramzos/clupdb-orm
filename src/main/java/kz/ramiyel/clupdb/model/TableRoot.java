package kz.ramiyel.clupdb.model;

import kz.ramiyel.clupdb.annotation.DBColumn;
import kz.ramiyel.clupdb.annotation.DBTable;
import kz.ramiyel.clupdb.exception.ColumnNotFoundException;
import kz.ramiyel.clupdb.exception.TableNotFoundException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class TableRoot<T> {

    private final Class<T> tableClass;
    private final DBTable table;
    private final Map<String, DBColumn> columns = new HashMap<>();

    public TableRoot(Class<T> tableClass) {
        this.tableClass = tableClass;
        if (tableClass.isAnnotationPresent(DBTable.class)) {
            this.table = tableClass.getAnnotation(DBTable.class);
            for (Field field : tableClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(DBColumn.class)) {
                    this.columns.put(field.getName(), field.getAnnotation(DBColumn.class));
                }
            }
        } else {
            throw new TableNotFoundException();
        }
    }

    public QuerySelection get(String variable) {
        if (this.columns.containsKey(variable)) {
            DBColumn column = this.columns.get(variable);
            return new QuerySelection(column.name(), variable);
        }
        throw new ColumnNotFoundException();
    }

    public Class<T> getTableClass() {
        return tableClass;
    }

    public DBTable getTable() {
        return table;
    }

    public Map<String, DBColumn> getColumns() {
        return columns;
    }
}
