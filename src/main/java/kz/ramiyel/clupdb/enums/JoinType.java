package kz.ramiyel.clupdb.enums;

public enum JoinType {
    SIMPLE("JOIN"),
    INNER("INNER JOIN"),
    LEFT("LEFT JOIN"),
    RIGHT("RIGHT JOIN"),
    FULL("FULL JOIN");

    private final String sql;

    JoinType(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
