package kz.ramiyel.clupdb.processor.type;

import kz.ramiyel.clupdb.base.type.DBSelectQuery;
import kz.ramiyel.clupdb.model.PreparedQuery;
import kz.ramiyel.clupdb.model.QueryHaving;
import kz.ramiyel.clupdb.model.QueryJoin;
import kz.ramiyel.clupdb.model.QueryOrderBy;
import kz.ramiyel.clupdb.model.QuerySelection;
import kz.ramiyel.clupdb.processor.QueryProcessor;
import java.util.stream.Collectors;

public class SelectQueryProcessor extends QueryProcessor {
    public SelectQueryProcessor(DBSelectQuery query, Object... parameters) {
        super(query, parameters);
    }

    @Override
    public PreparedQuery process() {
        DBSelectQuery query = (DBSelectQuery) getQuery();
        StringBuilder sql = new StringBuilder("SELECT ");
        if (query.getSelections().isEmpty()) {
            sql.append("*");
        } else {
            sql.append(
                    query.getSelections()
                            .stream()
                            .map(QuerySelection::toSql)
                            .collect(Collectors.joining(", "))
            );
        }
        sql.append(" FROM ").append(query.getTable().toSql());
        if (!query.getJoins().isEmpty()) {
            sql.append(
                    query.getJoins()
                            .stream()
                            .map(QueryJoin::toSql)
                            .collect(Collectors.joining("\n"))
            );
        }
        if (!query.getConditions().isEmpty()) {
            sql.append(" WHERE ")
                    .append(
                            query.getConditions()
                                    .stream()
                                    .map(dbCondition -> "(" + dbCondition.toSql() + ")")
                                    .collect(Collectors.joining(" AND "))
                    );
        }
        if (!query.getGroups().isEmpty()) {
            sql.append(" GROUP BY ")
                    .append(
                            query.getGroups().stream()
                                    .map(QuerySelection::getExpression)
                                    .collect(Collectors.joining(", "))
                    );
        }
        if (!query.getHavings().isEmpty()) {
            sql.append(" HAVING ")
                    .append(
                            query.getHavings()
                                    .stream()
                                    .map(QueryHaving::toSql)
                                    .collect(Collectors.joining(" AND "))
                    );
        }
        if (!query.getOrders().isEmpty()) {
            sql.append(" ORDER BY ")
                    .append(
                            query.getOrders()
                                    .stream()
                                    .map(QueryOrderBy::toSql)
                                    .collect(Collectors.joining(", "))
                    );
        }
        if (query.getLimit() != null) {
            sql.append(" LIMIT ").append(query.getLimit());
        }
        if (query.getOffset() != null) {
            sql.append(" OFFSET ").append(query.getOffset());
        }
        return new PreparedQuery(sql.toString(), getParameters());
    }
}
