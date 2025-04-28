package kz.ramiyel.clupdb.model;

import kz.ramiyel.clupdb.util.StringUtil;

public class QueryTable {
    private final String schema;
    private final String table;
    private final Class tableClazz;

    public QueryTable(String schema, String table, Class tableClazz) {
        this.schema = schema;
        this.table = table;
        this.tableClazz = tableClazz;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public Class getTableClazz() {
        return tableClazz;
    }

    public String toSql() {
        return StringUtil.isNotEmpty(schema) ? schema + "." + table : table;
    }
}
