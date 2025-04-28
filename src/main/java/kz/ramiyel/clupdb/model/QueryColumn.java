package kz.ramiyel.clupdb.model;

public class QueryColumn {
    private final QueryTable table;
    private final String column;

    public QueryColumn(QueryTable table, String column) {
        this.table = table;
        this.column = column;
    }

    public String toSql() {
        return table.toSql() + "." + column;
    }

    public QueryTable getTable() {
        return table;
    }

    public String getColumn() {
        return column;
    }
}
