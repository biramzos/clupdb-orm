package kz.ramiyel.clupdb.processor.type;

import kz.ramiyel.clupdb.base.type.DBInsertQuery;
import kz.ramiyel.clupdb.exception.ParameterException;
import kz.ramiyel.clupdb.model.PreparedQuery;
import kz.ramiyel.clupdb.model.QueryColumn;
import kz.ramiyel.clupdb.model.QueryTable;
import kz.ramiyel.clupdb.processor.QueryProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InsertQueryProcessor extends QueryProcessor {
    public InsertQueryProcessor(DBInsertQuery query, Object... parameters) {
        super(query, parameters);
    }

    @Override
    public PreparedQuery process() {
        DBInsertQuery insertQuery = (DBInsertQuery) getQuery();

        QueryTable table = insertQuery.getTable();
        List<QueryColumn> columns = insertQuery.getColumns();

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ")
                .append(table.toSql())
                .append(" (")
                .append(String.join(", ", columns.stream().map(QueryColumn::getColumn).toList()))
                .append(") VALUES ");

        if (insertQuery.getValues().size() > 0) {
            List<String> insertValues = new ArrayList<>();
            for(List<Object> value : insertQuery.getValues()) {
                String placeholders = columns.stream()
                        .map(v -> "?")
                        .collect(Collectors.joining(", "));
                insertValues.add("(" + placeholders + ")");
                getParameters().addAll(value);
            }
            sql.append(String.join(",\n", insertValues));
        } else {
            throw new ParameterException("Values are not set!");
        }
        return new PreparedQuery(sql.toString(), getParameters());
    }
}
