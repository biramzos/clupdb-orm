package kz.ramiyel.clupdb.manager;

import kz.ramiyel.clupdb.base.DBQuery;
import kz.ramiyel.clupdb.model.PreparedQuery;
import kz.ramiyel.clupdb.processor.QueryProcessor;
import java.sql.SQLException;
import java.util.List;

public class DBExecutor {

    private static PreparedQuery toPreparedQuery(DBQuery query, Object... parameters) {
        return QueryProcessor.getProcessor(query, parameters).process();
    }

    public static PreparedQuery getPreparedQuery(DBQuery query, Object... parameters) {
        return toPreparedQuery(query, parameters);
    }

    public static <T> List<T> getList(Class<T> clazz, DBQuery query, Object... parameters) {
        try {
            PreparedQuery preparedQuery = toPreparedQuery(query, parameters);
            return DB.getList(clazz, preparedQuery.getQuery(), preparedQuery.getParameters());
        } catch (SQLException e) {
            return null;
        }
    }

    public static <T> T getObject(Class<T> clazz, DBQuery query, Object... parameters) {
        try {
            PreparedQuery preparedQuery = toPreparedQuery(query, parameters);
            return DB.getObject(clazz, preparedQuery.getQuery(), preparedQuery.getParameters());
        } catch (SQLException e) {
            return null;
        }
    }

    public static <T> void execute(DBQuery query, Object... parameters) {
        try {
            PreparedQuery preparedQuery = toPreparedQuery(query, parameters);
            DB.execute(preparedQuery.getQuery(), preparedQuery.getParameters());
        } catch (SQLException ignored) {}
    }
}
