package kz.ramiyel.clupdb.generator.table;

import kz.ramiyel.clupdb.annotation.DBColumn;
import kz.ramiyel.clupdb.annotation.DBIndex;
import kz.ramiyel.clupdb.annotation.DBTable;
import kz.ramiyel.clupdb.model.TableModel;
import kz.ramiyel.clupdb.util.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class TableGenerator {

    private final List<TableModel> tables;

    public TableGenerator(List<TableModel> tables) {
        this.tables = tables;
    }

    public List<String> generate(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(DBTable.class)) return null;
        DBTable table = clazz.getAnnotation(DBTable.class);
        if (tables.stream().anyMatch(t -> t.getName().equalsIgnoreCase(table.name()))) return null;
        String tableName = StringUtil.isEmpty(table.name()) ? clazz.getSimpleName().toLowerCase() : table.name();
        String schema = StringUtil.isEmpty(table.schema()) ? "" : table.schema() + ".";

        List<String> columns = new ArrayList<>();
        List<String> constraints = new ArrayList<>();
        List<String> indexStatements = new ArrayList<>();
        List<String> commentStatements = new ArrayList<>();
        String fullTableName = schema + tableName;

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(DBColumn.class)) continue;
            DBColumn column = field.getAnnotation(DBColumn.class);

            StringBuilder sb = new StringBuilder();
            sb.append(column.name()).append(" ");

            if (column.autoIncrement()) {
                sb.append(column.type().equalsIgnoreCase("BIGINT") ? "BIGSERIAL" : "SERIAL");
            } else {
                sb.append(column.type());
            }

            if (column.notNull()) sb.append(" NOT NULL");
            if (column.unique()) sb.append(" UNIQUE");

            if (StringUtil.isNotEmpty(column.defaultValue())) {
                String type = column.type().toUpperCase();
                String defaultValue = column.defaultValue();

                if (type.contains("CHAR") || type.contains("TEXT") || type.contains("VARCHAR")) {
                    sb.append(" DEFAULT '").append(defaultValue).append("'");
                } else if (type.contains("BOOLEAN")) {
                    sb.append(" DEFAULT ").append(defaultValue.equalsIgnoreCase("true") ? "TRUE" : "FALSE");
                } else {
                    sb.append(" DEFAULT ").append(defaultValue);
                }
            }

            columns.add(sb.toString());

            if (column.primaryKey()) {
                constraints.add("PRIMARY KEY (" + column.name() + ")");
            }

            if (column.foreignKey()) {
                constraints.add("FOREIGN KEY (" + column.name() + ") REFERENCES "
                        + column.referencedTable() + "(" + column.referencedColumn() + ")");
            }

            if (StringUtil.isNotEmpty(column.comment())) {
                commentStatements.add("COMMENT ON COLUMN " + fullTableName + "." + column.name() + " IS '" + column.comment() + "';");
            }
        }

        StringJoiner columnDef = new StringJoiner(",\n  ", "(\n  ", "\n)");
        columns.forEach(columnDef::add);
        constraints.forEach(columnDef::add);
        List<String> queries = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ").append(fullTableName).append(" ").append(columnDef).append(";");

        queries.add(sqlBuilder.toString());
        if (StringUtil.isNotEmpty(table.comment())) {
            commentStatements.add("COMMENT ON TABLE " + fullTableName + " IS '" + table.comment() + "';");
        }
        queries.addAll(commentStatements);

        for (DBIndex index : table.indexes()) {
            indexStatements.add("CREATE INDEX " + index.name() + " ON " + fullTableName + " (" + index.columns() + ");");
        }
        queries.addAll(indexStatements);

        return queries;
    }


}
