package kz.ramiyel.clupdb.model;

import kz.ramiyel.clupdb.enums.DBOperator;
import kz.ramiyel.clupdb.exception.ValueNotFountException;

public class QueryCondition extends DBCondition {

    private final QuerySelection column;
    private final DBOperator operator;
    private final Object value;

    public QueryCondition(QuerySelection selection, DBOperator operator, Object value) {
        this.column = selection;
        this.operator = operator;
        this.value = value;
    }

    public QueryCondition(QuerySelection selection, DBOperator operator) {
        if (operator.isNeedValue()) {
            throw new ValueNotFountException();
        } else {
            this.column = selection;
            this.operator = operator;
            this.value = null;
        }
    }

    public QuerySelection getColumn() {
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
        if (column == null) {
            return "1 " + operator.getSql() + (operator.isNeedValue() ? " " + formatValue(value) : "");
        }
        return column + " " + operator.getSql() + (operator.isNeedValue() ? " " + formatValue(value) : "");
    }

    private String formatValue(Object value) {
        if (value instanceof String) {
            return "'" + value + "'";
        }
        if (value == null) {
            return "NULL";
        }
        if (value instanceof QuerySelection selection) {
            return selection.getExpression();
        }
        return String.valueOf(value);
    }

}
