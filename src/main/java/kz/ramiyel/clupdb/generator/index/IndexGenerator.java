package kz.ramiyel.clupdb.generator.index;

import kz.ramiyel.clupdb.annotation.DBTable;
import kz.ramiyel.clupdb.model.IndexModel;
import kz.ramiyel.clupdb.model.TableModel;
import kz.ramiyel.clupdb.util.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IndexGenerator {

    private final List<TableModel> tables;

    public IndexGenerator(List<TableModel> tables) {
        this.tables = tables;
    }

    public String generate(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(DBTable.class)) return null;
        DBTable tableAnnotation = clazz.getAnnotation(DBTable.class);
        String tableName = StringUtil.isEmpty(tableAnnotation.name()) ? clazz.getSimpleName().toLowerCase() : tableAnnotation.name();
        String schema = StringUtil.isEmpty(tableAnnotation.schema()) ? "" : tableAnnotation.schema();
        String fullTableName = schema.isEmpty() ? tableName : schema + "." + tableName;

        TableModel existingTable = findTable(tableName, schema);
        if (existingTable == null) return null;

        Map<String, IndexModel> existingIndexes = existingTable.getIndexes().stream()
                .collect(Collectors.toMap(IndexModel::getName, idx -> idx));

        List<String> indexStatements = new ArrayList<>();

        for (IndexModel index : existingTable.getIndexes()) {
            IndexModel existingIndex = existingIndexes.get(index.getName());

            if (existingIndex == null) {
                indexStatements.add(generateCreateIndex(fullTableName, index));
            }
        }

        if (indexStatements.isEmpty()) return null;

        return String.join("\n", indexStatements);
    }

    private String generateCreateIndex(String fullTableName, IndexModel index) {
        return "CREATE INDEX " + index.getName() +
                " ON " + fullTableName +
                " (" + index.getColumns() + ");";
    }

    private TableModel findTable(String tableName, String schema) {
        return tables.stream()
                .filter(t -> t.getName().equalsIgnoreCase(tableName) &&
                        (schema == null || schema.isEmpty() || schema.equalsIgnoreCase(t.getSchema())))
                .findFirst()
                .orElse(null);
    }
}
