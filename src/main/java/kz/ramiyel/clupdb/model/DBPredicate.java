package kz.ramiyel.clupdb.model;

import kz.ramiyel.clupdb.enums.DBOperator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DBPredicate extends DBCondition {

    public enum Operator {
        AND, OR
    }

    private final List<DBCondition> conditions;
    private final Operator operator;

    private DBPredicate(Operator operator, List<DBCondition> conditions) {
        this.operator = operator;
        this.conditions = conditions;
    }

    public static DBPredicate init() {
        return DBPredicate.and(new QueryCondition(null, DBOperator.EQUALS, 1));
    }

    public static DBPredicate and(DBCondition... conditions) {
        return new DBPredicate(Operator.AND, Arrays.asList(conditions));
    }

    public static DBPredicate or(DBCondition... conditions) {
        return new DBPredicate(Operator.OR, Arrays.asList(conditions));
    }

    @Override
    public String toSql() {
        return conditions.stream()
                .map(condition -> {
                    if (condition instanceof QueryCondition) {
                        return "(" + condition.toSql() + ")";
                    } else if (condition instanceof DBPredicate) {
                        return "(" + condition.toSql() + ")";
                    } else {
                        throw new IllegalArgumentException("Invalid condition type: " + condition.getClass());
                    }
                })
                .collect(Collectors.joining(" " + operator.name() + " "));
    }
}
