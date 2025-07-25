package kz.ramiyel.clupdb.manager;

import kz.ramiyel.clupdb.base.DBQuery;
import kz.ramiyel.clupdb.dtos.Pageable;
import kz.ramiyel.clupdb.dtos.TPage;
import kz.ramiyel.clupdb.model.PreparedQuery;
import kz.ramiyel.clupdb.processor.QueryProcessor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DBExecutor {

    private static PreparedQuery toPreparedQuery(DBQuery query, Object... parameters) {
        return QueryProcessor.getProcessor(query, parameters).process();
    }

    public static PreparedQuery getPreparedQuery(DBQuery query, Object... parameters) {
        return toPreparedQuery(query, parameters);
    }

    public static <T> TPage<T> getPage(Class<T> clazz, DBQuery query, Pageable pageable, Object... parameters) {
        return getPage(null, clazz, query, pageable, parameters);
    }

    public static <T> TPage<T> getPage(Connection con, Class<T> clazz, DBQuery query, Pageable pageable, Object... parameters) {
        try {
            PreparedQuery preparedQuery = toPreparedQuery(query, parameters);
            return DB.getPage(con, clazz, preparedQuery.getQuery(), pageable, preparedQuery.getParameters());
        } catch (SQLException e) {
            return null;
        }
    }

    public static <T> List<T> getList(Class<T> clazz, DBQuery query, Object... parameters) {
        return getList(null, clazz, query, parameters);
    }

    public static <T> List<T> getList(Connection con, Class<T> clazz, DBQuery query, Object... parameters) {
        try {
            PreparedQuery preparedQuery = toPreparedQuery(query, parameters);
            return DB.getList(con, clazz, preparedQuery.getQuery(), preparedQuery.getParameters());
        } catch (SQLException e) {
            return null;
        }
    }

    public static <T> T getObject(Class<T> clazz, DBQuery query, Object... parameters) {
        return getObject(null, clazz, query, parameters);
    }

    public static <T> T getObject(Connection con, Class<T> clazz, DBQuery query, Object... parameters) {
        try {
            PreparedQuery preparedQuery = toPreparedQuery(query, parameters);
            return DB.getObject(con, clazz, preparedQuery.getQuery(), preparedQuery.getParameters());
        } catch (SQLException e) {
            return null;
        }
    }

    public static <T> void execute(DBQuery query, Object... parameters) {
        execute(null, query, parameters);
    }

    public static <T> void execute(Connection con, DBQuery query, Object... parameters) {
        try {
            PreparedQuery preparedQuery = toPreparedQuery(query, parameters);
            DB.execute(con, preparedQuery.getQuery(), preparedQuery.getParameters());
        } catch (SQLException ignored) {}
    }
}
