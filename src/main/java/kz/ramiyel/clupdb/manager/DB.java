package kz.ramiyel.clupdb.manager;

import kz.ramiyel.clupdb.extractor.ResultSetExtractor;
import kz.ramiyel.clupdb.extractor.type.ListRowExtractor;
import kz.ramiyel.clupdb.extractor.type.SingleRowExtractor;
import kz.ramiyel.clupdb.mapper.RowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class DB {
    private static final Logger LOG = LoggerFactory.getLogger(DB.class);
    private static DataSource dataSource;

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static <T> List<T> getList(Class<T> clazz, String query, Object... parameters) throws SQLException {
        return getList(null, clazz, query, parameters);
    }

    public static <T> T getObject(Class<T> clazz, String query, Object... parameters) throws SQLException {
        return getObject(null, clazz, query, parameters);
    }

    public static <T> List<T> getList(Connection con, Class<T> clazz, String query, Object... parameters) throws SQLException {
        return runQuery(con, query, new ListRowExtractor<>(new RowMapper<>(clazz)), parameters);
    }

    public static <T> T getObject(Connection con, Class<T> clazz, String query, Object... parameters) throws SQLException {
        return runQuery(con, query, new SingleRowExtractor<>(new RowMapper<>(clazz)), parameters);
    }

    public static void execute(String query, Object... parameters) throws SQLException {
        execute(null, query, parameters);
    }

    public static void execute(Connection con, String query, Object... parameters) throws SQLException {
        runQuery(con, query, null, parameters);
    }

    private static <T> T runQuery(Connection con, String query, ResultSetExtractor<T> extractor, Object... parameters) throws SQLException {
        boolean selfConnection = Objects.isNull(con);
        try {
            if (selfConnection) {
                con = getConnection();
            }
            try (PreparedStatement ps = con.prepareStatement(query)) {
                return executeQuery(ps, extractor, parameters);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (selfConnection) {
                Objects.requireNonNull(con).close();
            }
        }
        return null;
    }

    private static <T> T executeQuery(PreparedStatement ps, ResultSetExtractor<T> extractor, Object... parameters) {
        try {
            setParameters(ps, parameters);
            if (Objects.nonNull(extractor)) {
                try (ResultSet rs = ps.executeQuery()) {
                    return extractor.extractData(rs);
                }
            } else {
                ps.execute();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    private static void setParameters(PreparedStatement ps, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            ps.setObject(i + 1, parameters[i]);
        }
    }

    public static void setDataSource(DataSource dataSource) {
        DB.dataSource = dataSource;
    }

}
