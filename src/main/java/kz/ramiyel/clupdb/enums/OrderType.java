package kz.ramiyel.clupdb.enums;

public enum OrderType {
    ASC("ASC"),
    DESC("DESC");

    private final String sql;

    OrderType(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
