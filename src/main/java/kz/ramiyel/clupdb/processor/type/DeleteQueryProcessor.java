package kz.ramiyel.clupdb.processor.type;

import kz.ramiyel.clupdb.base.type.DBDeleteQuery;
import kz.ramiyel.clupdb.model.PreparedQuery;
import kz.ramiyel.clupdb.processor.QueryProcessor;
import java.util.stream.Collectors;

public class DeleteQueryProcessor extends QueryProcessor {
    public DeleteQueryProcessor(DBDeleteQuery query, Object... parameters) {
        super(query, parameters);
    }

    @Override
    public PreparedQuery process() {
        DBDeleteQuery deleteQuery = (DBDeleteQuery) getQuery();
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(deleteQuery.getTable().toSql());

        if (!deleteQuery.getConditions().isEmpty()) {
            sql.append(" WHERE ")
                    .append(
                            deleteQuery.getConditions()
                                    .stream()
                                    .map(dbCondition -> {
                                        getParameters().addAll(dbCondition.getParameters());
                                        return "(" + dbCondition.toSql() + ")";
                                    })
                                    .collect(Collectors.joining(" AND "))
                    );
        }

        return new PreparedQuery(sql.toString(), getParameters());
    }
}
