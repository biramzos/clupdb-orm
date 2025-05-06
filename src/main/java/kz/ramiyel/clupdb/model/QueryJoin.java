package kz.ramiyel.clupdb.model;

import kz.ramiyel.clupdb.enums.JoinType;
import kz.ramiyel.clupdb.util.TableUtil;

public class QueryJoin<From, Join> {
    private Class<From> fromClass;
    private Class<Join> joinClass;
    private JoinType joinType;
    private String fromVariable;
    private String joinVariable;
    private String[] additionalConditions;

    public QueryJoin(Class<From> fromClass, Class<Join> joinClass, JoinType joinType, String fromVariable, String joinVariable, String... additionalConditions) {
        this.fromClass = fromClass;
        this.joinClass = joinClass;
        this.joinType = joinType;
        this.fromVariable = fromVariable;
        this.joinVariable = joinVariable;
        this.additionalConditions = additionalConditions;
    }

    public static <F, J> QueryJoin<F, J> of(Class<F> fromClass, Class<J> joinClass, JoinType joinType, String fromVariable, String joinVariable, String... additionalConditions) {
        return new QueryJoin<>(fromClass, joinClass, joinType, fromVariable, joinVariable, additionalConditions);
    }

    public Class<From> getFromClass() {
        return fromClass;
    }

    public Class<Join> getJoinClass() {
        return joinClass;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public String getFromVariable() {
        return fromVariable;
    }

    public String getJoinVariable() {
        return joinVariable;
    }

    public String toSql() {
        QueryTable fromTable = TableUtil.parseTable(fromClass);
        QueryTable joinTable = TableUtil.parseTable(joinClass);

        QueryColumn fromColumn = TableUtil.parseColumn(fromClass, fromTable, fromVariable);
        QueryColumn joinColumn = TableUtil.parseColumn(joinClass, joinTable, joinVariable);

        if (this.additionalConditions.length > 0) {
            return String.format("%s %s ON %s = %s AND %s",
                    joinType.getSql(),
                    joinTable.toSql(),
                    fromColumn.toSql(),
                    joinColumn.toSql(),
                    String.join(" AND ", additionalConditions)
            );
        }
        return String.format("%s %s ON %s = %s",
                joinType.getSql(),
                joinTable.toSql(),
                fromColumn.toSql(),
                joinColumn.toSql()
        );
    }
}
