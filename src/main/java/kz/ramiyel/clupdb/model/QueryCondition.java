package kz.ramiyel.clupdb.model;

import kz.ramiyel.clupdb.enums.DBOperator;

public class QueryCondition extends DBCondition {

    private String column;
    private DBOperator operator;
    private Object value;

    public QueryCondition(String column, DBOperator operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public DBOperator getOperator() {
        return operator;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toSql() {
        return column + " " + operator.getSql() + " " + formatValue(value);
    }

    private String formatValue(Object value) {
        if (value instanceof String) {
            return "'" + value + "'";
        }
        if (value == null) {
            return "NULL";
        }
        return String.valueOf(value);
    }

}
