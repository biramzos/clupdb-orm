package kz.ramiyel.clupdb.generator.index;

import kz.ramiyel.clupdb.annotation.DBIndex;
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

    public List<String> generate(Class<?> clazz) {
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

        for (DBIndex index : tableAnnotation.indexes()) {
            IndexModel existingIndex = existingIndexes.getOrDefault(index.name(), null);

            if (existingIndex == null) {
                indexStatements.add(generateCreateIndex(fullTableName, index));
            }
        }

        if (indexStatements.isEmpty()) return new ArrayList<>();

        return indexStatements;
    }

    private String generateCreateIndex(String fullTableName, DBIndex index) {
        return "CREATE INDEX " + index.name() +
                " ON " + fullTableName +
                " (" + index.columns() + ");";
    }

    private TableModel findTable(String tableName, String schema) {
        return tables.stream()
                .filter(t -> schema == null || schema.isEmpty() || schema.equalsIgnoreCase(t.getSchema()))
                .filter(t -> t.getName().equalsIgnoreCase(tableName))
                .findFirst()
                .orElse(null);
    }
}
