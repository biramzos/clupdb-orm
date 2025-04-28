package kz.ramiyel.clupdb.enums;

public enum DBOperator {
    EQUALS("=", true),
    NOT_EQUALS("<>", true),
    GREATER_THAN(">", true),
    GREATER_OR_EQUALS(">=", true),
    LESS_THAN("<", true),
    LESS_OR_EQUALS("<=", true),
    LIKE("LIKE", true),
    IN("IN", true),
    IS_NULL("IS NULL", false),
    IS_NOT_NULL("IS NOT NULL", false);

    private final String sql;
    private final boolean needValue;

    DBOperator(String sql, boolean needValue) {
        this.sql = sql;
        this.needValue = needValue;
    }

    public String getSql() {
        return sql;
    }

    public boolean isNeedValue() {
        return needValue;
    }
}
