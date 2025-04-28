package kz.ramiyel.clupdb.model;

import kz.ramiyel.clupdb.util.StringUtil;

public class QuerySelection {

    private final String expression;
    private final String alias;

    public QuerySelection(String expression, String alias) {
        this.expression = expression;
        this.alias = alias;
    }

    public String getExpression() {
        return expression;
    }

    public String getAlias() {
        return alias;
    }

    public String toSql() {
        return expression + (StringUtil.isNotEmpty(alias) ? " AS " + alias : "");
    }

}
