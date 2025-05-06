package kz.ramiyel.clupdb.processor.type;

import kz.ramiyel.clupdb.base.type.DBUpdateQuery;
import kz.ramiyel.clupdb.model.PreparedQuery;
import kz.ramiyel.clupdb.model.QueryExpression;
import kz.ramiyel.clupdb.model.QuerySelection;
import kz.ramiyel.clupdb.model.QuerySelectionValue;
import kz.ramiyel.clupdb.processor.QueryProcessor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateQueryProcessor extends QueryProcessor {
    public UpdateQueryProcessor(DBUpdateQuery query, Object... parameters) {
        super(query, parameters);
    }

    @Override
    public PreparedQuery process() {
        DBUpdateQuery updateQuery = (DBUpdateQuery) getQuery();

        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(updateQuery.getTable().toSql()).append(" SET ");
        List<String> assignments = new ArrayList<>();
        for (QuerySelectionValue selection : updateQuery.getSelections()) {
            if (selection.getValue() instanceof QueryExpression expression) {
                assignments.add(selection.getSelection().getExpression() + " = " + expression.toSql());
            } else if (selection.getValue() instanceof QuerySelection select) {
                assignments.add(selection.getSelection().getExpression() + " = " + select.getExpression());
            } else {
                assignments.add(selection.getSelection().getExpression() + " = ?");
                getParameters().add(selection.getValue());
            }
        }

        query.append(String.join(", ", assignments));

        if (!updateQuery.getConditions().isEmpty()) {
            query.append(" WHERE ")
                    .append(
                            updateQuery.getConditions()
                                    .stream()
                                    .map(dbCondition -> {
                                        getParameters().addAll(dbCondition.getParameters());
                                        return "(" + dbCondition.toSql() + ")";
                                    })
                                    .collect(Collectors.joining(" AND "))
                    );
        }

        return new PreparedQuery(query.toString(), getParameters());
    }
}
