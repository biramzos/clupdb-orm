package kz.ramiyel.clupdb.model;

import kz.ramiyel.clupdb.enums.DBOperator;
import kz.ramiyel.clupdb.exception.ValueNotFountException;
import kz.ramiyel.clupdb.util.ValueUtil;

public class QueryCondition extends DBCondition {

    private final QuerySelection column;
    private final DBOperator operator;
    private final Object value;

    public QueryCondition(QuerySelection selection, DBOperator operator, Object value) {
        this.column = selection;
        this.operator = operator;
        this.value = value;
    }

    public QueryCondition(QuerySelection selection, DBOperator operator, QuerySelection value) {
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

    public static QueryCondition of(QuerySelection selection, DBOperator operator, Object value) {
        return new QueryCondition(selection, operator, value);
    }

    public static QueryCondition of(QuerySelection selection, DBOperator operator, QuerySelection value) {
        return new QueryCondition(selection, operator, value);
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
            return "1 " + operator.getSql() + (operator.isNeedValue() ? " " + ValueUtil.formatValue(value) : "");
        }
        return column.getExpression() + " " + operator.getSql() + (operator.isNeedValue() ? " " + ValueUtil.formatValue(value) : "");
    }

}
