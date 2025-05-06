package kz.ramiyel.clupdb.model;

import kz.ramiyel.clupdb.enums.DBOperator;
import kz.ramiyel.clupdb.exception.ValueNotFountException;
import kz.ramiyel.clupdb.util.ValueUtil;

public class QueryHaving extends DBCondition {
    private final QueryExpression expression;
    private final DBOperator operator;
    private final Object value;

    public QueryHaving(QueryExpression selection, DBOperator operator, Object value) {
        this.expression = selection;
        this.operator = operator;
        this.value = value;
    }

    public QueryHaving(QueryExpression selection, DBOperator operator, QueryExpression value) {
        this.expression = selection;
        this.operator = operator;
        this.value = value;
    }

    public QueryHaving(QueryExpression selection, DBOperator operator, QuerySelection value) {
        this.expression = selection;
        this.operator = operator;
        this.value = value;
    }

    public QueryHaving(QueryExpression selection, DBOperator operator) {
        if (operator.isNeedValue()) {
            throw new ValueNotFountException();
        } else {
            this.expression = selection;
            this.operator = operator;
            this.value = null;
        }
    }

    public static QueryHaving of(QueryExpression selection, DBOperator operator, Object value) {
        return new QueryHaving(selection, operator, value);
    }

    public static QueryHaving of(QueryExpression selection, DBOperator operator, QueryExpression value) {
        return new QueryHaving(selection, operator, value);
    }

    public static QueryHaving of(QueryExpression selection, DBOperator operator, QuerySelection value) {
        return new QueryHaving(selection, operator, value);
    }

    public QueryExpression getExpression() {
        return expression;
    }

    public DBOperator getOperator() {
        return operator;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(expression.toSql()).append(" ");
        sql.append(operator.getSql());
        if (operator.isNeedValue()) {
            sql.append(" ");
            if (value instanceof QueryExpression exp) {
                sql.append(exp.toSql());
            } else if (value instanceof QuerySelection selection) {
                sql.append(selection.getExpression());
            } else {
                getParameters().add(value);
                return "?";
            }
        }
        return sql.toString();
    }
}
