package kz.ramiyel.clupdb.model;

import kz.ramiyel.clupdb.util.StringUtil;

public class QuerySelection {
    private final QueryTable table;
    private final String expression;
    private final String alias;

    public QuerySelection(QueryTable table, String expression, String alias) {
        this.table = table;
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
