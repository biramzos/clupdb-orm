package kz.ramiyel.clupdb.enums;

public enum DBOperator {
    EQUALS("="),
    NOT_EQUALS("<>"),
    GREATER_THAN(">"),
    GREATER_OR_EQUALS(">="),
    LESS_THAN("<"),
    LESS_OR_EQUALS("<="),
    LIKE("LIKE"),
    IN("IN"),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL");

    private final String sql;

    DBOperator(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
