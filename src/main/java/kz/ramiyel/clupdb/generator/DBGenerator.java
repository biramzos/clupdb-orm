package kz.ramiyel.clupdb.generator;

import kz.ramiyel.clupdb.annotation.DBTable;
import kz.ramiyel.clupdb.generator.column.ColumnGenerator;
import kz.ramiyel.clupdb.generator.index.IndexGenerator;
import kz.ramiyel.clupdb.generator.table.TableGenerator;
import kz.ramiyel.clupdb.model.ColumnModel;
import kz.ramiyel.clupdb.model.IndexModel;
import kz.ramiyel.clupdb.model.TableModel;
import kz.ramiyel.clupdb.manager.DB;
import kz.ramiyel.clupdb.type.ColumnType;
import kz.ramiyel.clupdb.util.StringUtil;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DBGenerator {
    private final Reflections reflections;
    private final String schema;
    private final int batchSize;

    public DBGenerator(Reflections reflections) {
        this(reflections, "public", 100);
    }

    public DBGenerator(Reflections reflections, String schema) {
        this(reflections, schema, 100);
    }

    public DBGenerator(Reflections reflections, int batchSize) {
        this(reflections, "public", batchSize);
    }

    public DBGenerator(Reflections reflections, String schema, int batchSize) {
        this.reflections = reflections;
        this.schema = schema;
        this.batchSize = batchSize;
    }

    private static final Logger LOG = LoggerFactory.getLogger(DBGenerator.class);

    public void execute() {
        try (Connection con = DB.getConnection()) {
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();

            List<TableModel> metadata = getTables(con, schema);

            TableGenerator tableGenerator = new TableGenerator(metadata);
            ColumnGenerator columnGenerator = new ColumnGenerator(metadata);
            IndexGenerator indexGenerator = new IndexGenerator(metadata);

            Set<Class<?>> tables = reflections.getTypesAnnotatedWith(DBTable.class);

            List<String> tableBatch = new ArrayList<>();
            List<String> columnBatch = new ArrayList<>();
            List<String> indexBatch = new ArrayList<>();

            for (Class<?> table : tables) {
                List<String> tableSql = tableGenerator.generate(table);
                if (Objects.nonNull(tableSql) && !tableSql.isEmpty()) {
                    tableBatch.addAll(tableSql);
                }

                List<String> columnSql = columnGenerator.generate(table);
                if (Objects.nonNull(columnSql) && !columnSql.isEmpty()) {
                    columnBatch.addAll(columnSql);
                }

                List<String> indexSql = indexGenerator.generate(table);
                if (Objects.nonNull(indexSql) && !indexSql.isEmpty()) {
                    indexBatch.addAll(indexSql);
                }

                if (tableBatch.size() >= batchSize) {
                    executeBatchAndClear(stmt, tableBatch);
                }
                if (columnBatch.size() >= batchSize) {
                    executeBatchAndClear(stmt, columnBatch);
                }
                if (indexBatch.size() >= batchSize) {
                    executeBatchAndClear(stmt, indexBatch);
                }
            }

            if (!tableBatch.isEmpty()) {
                executeBatchAndClear(stmt, tableBatch);
            }
            if (!columnBatch.isEmpty()) {
                executeBatchAndClear(stmt, columnBatch);
            }
            if (!indexBatch.isEmpty()) {
                executeBatchAndClear(stmt, indexBatch);
            }
            con.commit();

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void executeBatchAndClear(Statement stmt, List<String> batch) throws SQLException {
        try {
            for (String query : batch) {
                stmt.addBatch(query);
            }
            int[] updateCounts = stmt.executeBatch();
            for (int i = 0; i < updateCounts.length; i++) {
                if (updateCounts[i] < 0) {
                    LOG.error("Batch entry " + i + " failed with a negative result: " + updateCounts[i]);
                }
            }
        } catch (BatchUpdateException e) {
            for (Throwable t : e) {
                LOG.error("Error processing batch entry", t);
            }
            Throwable nextException = e.getNextException();
            if (nextException != null) {
                LOG.error("Next exception: ", nextException);
            }
        } finally {
            stmt.clearBatch();
        }
    }


    public List<TableModel> getTables(Connection con, String schema) throws SQLException {
        List<TableModel> tables = new ArrayList<>();
        String sql = "SELECT table_name FROM information_schema.tables " +
                "WHERE table_schema = ? AND table_type = 'BASE TABLE'";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, schema);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String tableName = rs.getString("table_name");
                    tables.add(getTableInfo(con, schema, tableName));
                }
            }
        }
        return tables;
    }


    public TableModel getTableInfo(Connection con, String schema, String tableName) throws SQLException {
        TableModel table = new TableModel();
        table.setName(tableName);
        table.setSchema(schema);
        table.setColumns(new ArrayList<>());
        table.setIndexes(new ArrayList<>());

        String tableCommentSql = "SELECT obj_description(('\"' || table_schema || '\".\"' || table_name || '\"')::regclass) AS comment " +
                "FROM information_schema.tables WHERE table_schema = ? AND table_name = ?";
        try (PreparedStatement stmt = con.prepareStatement(tableCommentSql)) {
            stmt.setString(1, schema);
            stmt.setString(2, tableName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    table.setComment(rs.getString("comment"));
                }
            }
        }

        String columnSql = "SELECT c.column_name, c.data_type, c.is_nullable, c.column_default, " +
                "tc.constraint_type, kcu2.table_name AS foreign_table, kcu2.column_name AS foreign_column " +
                "FROM information_schema.columns c " +
                "LEFT JOIN information_schema.key_column_usage kcu " +
                "  ON c.table_name = kcu.table_name AND c.column_name = kcu.column_name AND c.table_schema = kcu.table_schema " +
                "LEFT JOIN information_schema.table_constraints tc " +
                "  ON kcu.constraint_name = tc.constraint_name AND tc.table_schema = c.table_schema " +
                "LEFT JOIN information_schema.referential_constraints rc " +
                "  ON tc.constraint_name = rc.constraint_name " +
                "LEFT JOIN information_schema.key_column_usage kcu2 " +
                "  ON rc.unique_constraint_name = kcu2.constraint_name AND kcu2.table_schema = c.table_schema " +
                "WHERE c.table_schema = ? AND c.table_name = ? " +
                "ORDER BY c.ordinal_position";

        try (PreparedStatement stmt = con.prepareStatement(columnSql)) {
            stmt.setString(1, schema);
            stmt.setString(2, tableName);
            try (ResultSet rs = stmt.executeQuery()) {
                Map<String, ColumnModel> columnMap = new LinkedHashMap<>();

                while (rs.next()) {
                    String columnName = rs.getString("column_name");
                    ColumnModel column = columnMap.getOrDefault(columnName, new ColumnModel());
                    column.setName(columnName);
                    column.setType(ColumnType.parseFrom(rs.getString("data_type")));
                    column.setNotNull("NO".equals(rs.getString("is_nullable")));
                    column.setDefaultValue(rs.getString("column_default"));

                    String constraintType = rs.getString("constraint_type");
                    if ("PRIMARY KEY".equals(constraintType)) {
                        column.setPrimaryKey(true);
                        column.setNotNull(true);
                    } else if ("UNIQUE".equals(constraintType)) {
                        column.setUnique(true);
                    } else if ("FOREIGN KEY".equals(constraintType)) {
                        column.setForeignKey(true);
                        column.setReferencedTable(rs.getString("foreign_table"));
                        column.setReferencedColumn(rs.getString("foreign_column"));
                    }

                    String defaultValue = rs.getString("column_default");
                    if (defaultValue != null && defaultValue.startsWith("nextval(")) {
                        column.setAutoIncrement(true);
                    }

                    columnMap.put(columnName, column);
                }

                table.getColumns().addAll(columnMap.values());
            }
        }

        String columnCommentSql = "SELECT a.attname AS column_name, d.description AS comment " +
                "FROM pg_catalog.pg_attribute a " +
                "JOIN pg_catalog.pg_class c ON a.attrelid = c.oid " +
                "JOIN pg_catalog.pg_namespace n ON c.relnamespace = n.oid " +
                "LEFT JOIN pg_catalog.pg_description d ON d.objoid = a.attrelid AND d.objsubid = a.attnum " +
                "WHERE n.nspname = ? AND c.relname = ? AND a.attnum > 0";
        try (PreparedStatement stmt = con.prepareStatement(columnCommentSql)) {
            stmt.setString(1, schema);
            stmt.setString(2, tableName);
            try (ResultSet rs = stmt.executeQuery()) {
                Map<String, String> columnComments = new HashMap<>();
                while (rs.next()) {
                    columnComments.put(rs.getString("column_name"), rs.getString("comment"));
                }
                for (ColumnModel column : table.getColumns()) {
                    column.setComment(columnComments.getOrDefault(column.getName(), null));
                }
            }
        }

        String indexSql = "SELECT i.relname AS index_name, " +
                "array_to_string(array_agg(a.attname), ',') AS columns " +
                "FROM pg_class t, pg_class i, pg_index ix, pg_attribute a, pg_namespace n " +
                "WHERE t.oid = ix.indrelid AND i.oid = ix.indexrelid AND a.attrelid = t.oid " +
                "AND a.attnum = ANY(ix.indkey) AND t.relkind = 'r' " +
                "AND n.oid = t.relnamespace AND n.nspname = ? AND t.relname = ? " +
                "GROUP BY i.relname";
        try (PreparedStatement stmt = con.prepareStatement(indexSql)) {
            stmt.setString(1, schema);
            stmt.setString(2, tableName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    IndexModel index = new IndexModel();
                    index.setName(rs.getString("index_name"));
                    index.setColumns(rs.getString("columns"));
                    table.getIndexes().add(index);
                }
            }
        }
        return table;
    }
}
