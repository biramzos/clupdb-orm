package kz.ramiyel.clupdb.processor;

import kz.ramiyel.clupdb.base.DBQuery;
import kz.ramiyel.clupdb.base.type.DBDeleteQuery;
import kz.ramiyel.clupdb.base.type.DBInsertQuery;
import kz.ramiyel.clupdb.base.type.DBSelectQuery;
import kz.ramiyel.clupdb.base.type.DBUpdateQuery;
import kz.ramiyel.clupdb.exception.UnknownQueryTypeException;
import kz.ramiyel.clupdb.model.PreparedQuery;
import kz.ramiyel.clupdb.processor.type.DeleteQueryProcessor;
import kz.ramiyel.clupdb.processor.type.InsertQueryProcessor;
import kz.ramiyel.clupdb.processor.type.SelectQueryProcessor;
import kz.ramiyel.clupdb.processor.type.UpdateQueryProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class QueryProcessor {

    private final DBQuery query;
    private final List<Object> parameters;

    public QueryProcessor(DBQuery query, Object... parameters) {
        this.query = query;
        this.parameters = new ArrayList<>();
        this.parameters.addAll(Arrays.stream(parameters).toList());
    }

    public abstract PreparedQuery process();

    public DBQuery getQuery() {
        return query;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public static <T extends QueryProcessor> T getProcessor(DBQuery query, Object... parameters) {
        if (query instanceof DBSelectQuery selectQuery) {
            return (T) new SelectQueryProcessor(selectQuery, parameters);
        } else if (query instanceof DBInsertQuery insertQuery) {
            return (T) new InsertQueryProcessor(insertQuery, parameters);
        } else if (query instanceof DBUpdateQuery updateQuery) {
            return (T) new UpdateQueryProcessor(updateQuery, parameters);
        } else if (query instanceof DBDeleteQuery deleteQuery) {
            return (T) new DeleteQueryProcessor(deleteQuery, parameters);
        } else {
            throw new UnknownQueryTypeException();
        }
    }
}
