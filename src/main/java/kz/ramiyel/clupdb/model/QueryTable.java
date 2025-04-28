package kz.ramiyel.clupdb.model;

import kz.ramiyel.clupdb.util.StringUtil;

public class QueryTable {
    private String schema;
    private String table;

    public QueryTable(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String toSql() {
        return StringUtil.isNotEmpty(schema) ? schema + "." + table : table;
    }
}
