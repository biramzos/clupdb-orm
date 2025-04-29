package kz.ramiyel.clupdb.model;

import kz.ramiyel.clupdb.util.ValueUtil;

import java.util.Arrays;
import java.util.List;

public class QueryExpression {
    private final QuerySelection column;
    private final String function;
    private final Object[] otherParameters;

    public QueryExpression(QuerySelection column, String function, Object... parameters) {
        this.column = column;
        this.function = function;
        this.otherParameters = parameters;
    }

    public QueryExpression of(QuerySelection column, String function, Object... parameters) {
        return new QueryExpression(column, function, parameters);
    }

    public QuerySelection getColumn() {
        return column;
    }

    public String getFunction() {
        return function;
    }

    public Object[] getOtherParameters() {
        return otherParameters;
    }

    public String toSql() {
        StringBuilder builder = new StringBuilder();
        builder.append(function)
                .append("(")
                .append(column.getExpression());
        List<String> params = Arrays.stream(otherParameters).map(ValueUtil::formatValue).toList();
        if (!params.isEmpty()) {
            builder.append(",").append(String.join(",", params));
        }
        builder.append(")");
        return builder.toString();
    }
}
