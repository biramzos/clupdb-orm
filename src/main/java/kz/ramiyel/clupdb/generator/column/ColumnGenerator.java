package kz.ramiyel.clupdb.generator.column;

import kz.ramiyel.clupdb.annotation.DBColumn;
import kz.ramiyel.clupdb.annotation.DBTable;
import kz.ramiyel.clupdb.model.ColumnModel;
import kz.ramiyel.clupdb.model.TableModel;
import kz.ramiyel.clupdb.type.ColumnType;
import kz.ramiyel.clupdb.util.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ColumnGenerator {

    private final List<TableModel> tables;

    public ColumnGenerator(List<TableModel> tables) {
        this.tables = tables;
    }

    public List<String> generate(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(DBTable.class)) return null;

        DBTable tableAnnotation = clazz.getAnnotation(DBTable.class);
        String tableName = StringUtil.isEmpty(tableAnnotation.name()) ? clazz.getSimpleName().toLowerCase() : tableAnnotation.name();
        String schema = StringUtil.isEmpty(tableAnnotation.schema()) ? "" : tableAnnotation.schema();
        String fullTableName = schema.isEmpty() ? tableName : schema + "." + tableName;

        TableModel existingTable = findTable(tableName, schema);
        if (existingTable == null) return null;

        Map<String, ColumnModel> existingColumns = existingTable.getColumns().stream()
                .collect(Collectors.toMap(ColumnModel::getName, col -> col));

        List<String> alterStatements = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(DBColumn.class)) continue;
            DBColumn columnAnnotation = field.getAnnotation(DBColumn.class);

            String columnName = columnAnnotation.name();
            ColumnModel existing = existingColumns.get(columnName);

            if (existing == null) {
                alterStatements.add(generateAddColumn(fullTableName, columnAnnotation));
            } else {
                List<String> columnAlterations = generateAlterColumn(fullTableName, columnAnnotation, existing);
                alterStatements.addAll(columnAlterations);
            }
        }

        if (alterStatements.isEmpty()) return new ArrayList<>();

        return alterStatements;
    }

    private String generateAddColumn(String fullTableName, DBColumn column) {
        StringBuilder sb = new StringBuilder();
        sb.append("ALTER TABLE ").append(fullTableName).append(" ADD COLUMN ");
        sb.append(column.name()).append(" ");

        if (column.autoIncrement()) {
            sb.append(column.type() == ColumnType.BIGINT ? "BIGSERIAL" : "SERIAL");
        } else {
            sb.append(column.type());
        }

        if (column.notNull()) sb.append(" NOT NULL");
        if (column.unique()) sb.append(" UNIQUE");
        if (!StringUtil.isEmpty(column.defaultValue())) {
            sb.append(" DEFAULT ").append(formatDefaultValue(column.defaultValue(), column.type()));
        }
        sb.append(";");

        if (!StringUtil.isEmpty(column.comment())) {
            sb.append("\nCOMMENT ON COLUMN ").append(fullTableName).append(".").append(column.name())
                    .append(" IS '").append(column.comment().replace("'", "''")).append("';");
        }

        return sb.toString();
    }

    private List<String> generateAlterColumn(String fullTableName, DBColumn column, ColumnModel existing) {
        List<String> alterations = new ArrayList<>();

        if (!StringUtil.equalsIgnoreCase(column.type().name(), existing.getType().name())) {
            alterations.add("ALTER TABLE " + fullTableName + " ALTER COLUMN " + column.name()
                    + " TYPE " + column.type() + ";");
        }

        if (column.notNull() != existing.isNotNull()) {
            if (column.notNull()) {
                alterations.add("ALTER TABLE " + fullTableName + " ALTER COLUMN " + column.name() + " SET NOT NULL;");
            } else {
                alterations.add("ALTER TABLE " + fullTableName + " ALTER COLUMN " + column.name() + " DROP NOT NULL;");
            }
        }

        if (!StringUtil.equalsNullableString(column.defaultValue(), existing.getDefaultValue())) {
            if (StringUtil.isEmpty(column.defaultValue())) {
                alterations.add("ALTER TABLE " + fullTableName + " ALTER COLUMN " + column.name() + " DROP DEFAULT;");
            } else {
                alterations.add("ALTER TABLE " + fullTableName + " ALTER COLUMN " + column.name()
                        + " SET DEFAULT " + formatDefaultValue(column.defaultValue(), column.type()) + ";");
            }
        }

        if (!StringUtil.equalsNullableString(column.comment(), existing.getComment())) {
            alterations.add("COMMENT ON COLUMN " + fullTableName + "." + column.name()
                    + " IS '" + column.comment().replace("'", "''") + "';");
        }

        return alterations;
    }

    private TableModel findTable(String tableName, String schema) {
        return tables.stream()
                .filter(t -> t.getName().equalsIgnoreCase(tableName) &&
                        (schema == null || schema.isEmpty() || schema.equalsIgnoreCase(t.getSchema())))
                .findFirst()
                .orElse(null);
    }

    private String formatDefaultValue(String defaultValue, ColumnType type) {
        if (type.name().contains("VARCHAR") || type == ColumnType.TEXT || type.name().toLowerCase().contains("char")) {
            return "'" + defaultValue.replace("'", "''") + "'";
        }
        return defaultValue;
    }
}
