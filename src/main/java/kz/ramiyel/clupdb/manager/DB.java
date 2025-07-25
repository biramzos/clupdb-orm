package kz.ramiyel.clupdb.manager;

import kz.ramiyel.clupdb.constants.BasicColumnConstants;
import kz.ramiyel.clupdb.constants.PropertyConstants;
import kz.ramiyel.clupdb.constants.QueryPatternConstants;
import kz.ramiyel.clupdb.dtos.Pageable;
import kz.ramiyel.clupdb.dtos.TPage;
import kz.ramiyel.clupdb.extractor.ResultSetExtractor;
import kz.ramiyel.clupdb.extractor.type.ListRowExtractor;
import kz.ramiyel.clupdb.extractor.type.PageRowExtractor;
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
import java.util.regex.Matcher;

public class DB {
    private static final Logger LOG = LoggerFactory.getLogger(DB.class);
    private static DataSource dataSource;

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static <T> TPage<T> getPage(Class<T> clazz, String query, Pageable pageable, Object... parameters) throws SQLException {
        return getPage(null, clazz, query, pageable, parameters);
    }

    public static <T> List<T> getList(Class<T> clazz, String query, Object... parameters) throws SQLException {
        return getList(null, clazz, query, parameters);
    }

    public static <T> T getObject(Class<T> clazz, String query, Object... parameters) throws SQLException {
        return getObject(null, clazz, query, parameters);
    }

    public static <T> TPage<T> getPage(Connection con, Class<T> clazz, String query, Pageable pageable, Object... parameters) throws SQLException {
        showQuery(query, parameters);
        return runQuery(con, normalizePageQuery(query, pageable), new PageRowExtractor<>(new RowMapper<>(clazz)), parameters);
    }

    public static <T> List<T> getList(Connection con, Class<T> clazz, String query, Object... parameters) throws SQLException {
        showQuery(query, parameters);
        return runQuery(con, query, new ListRowExtractor<>(new RowMapper<>(clazz)), parameters);
    }

    public static <T> T getObject(Connection con, Class<T> clazz, String query, Object... parameters) throws SQLException {
        showQuery(query, parameters);
        return runQuery(con, query, new SingleRowExtractor<>(new RowMapper<>(clazz)), parameters);
    }

    public static void execute(String query, Object... parameters) throws SQLException {
        execute(null, query, parameters);
    }

    public static void execute(Connection con, String query, Object... parameters) throws SQLException {
        showQuery(query, parameters);
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

    private static String normalizePageQuery(String sql, Pageable pageable) {
        Matcher countMatcher = QueryPatternConstants.COUNT_OVER_PATTERN.matcher(sql);
        if (countMatcher.find()) {
            String overClause = countMatcher.group(1) != null ? countMatcher.group(1) : "()";
            String replacement = "COUNT(*) OVER " + overClause + " AS " + BasicColumnConstants.TOTAL_COLUMN_NAME;
            sql = countMatcher.replaceFirst(Matcher.quoteReplacement(replacement));
        } else {
            int fromIndex = sql.toLowerCase().indexOf("from");
            if (fromIndex != -1) {
                String before = sql.substring(0, fromIndex).trim();
                String after = sql.substring(fromIndex);
                sql = before + ", COUNT(*) OVER() AS " + BasicColumnConstants.TOTAL_COLUMN_NAME + " " + after;
            }
        }
        Matcher limitMatcher = QueryPatternConstants.LIMIT_PATTERN.matcher(sql);
        if (limitMatcher.find()) {
            String newLimit = pageable.toString();
            sql = limitMatcher.replaceFirst(Matcher.quoteReplacement(newLimit));
        } else {
            sql = sql.trim();
            if (!sql.toLowerCase().endsWith(";")) {
                sql += pageable.toString();
            } else {
                sql = sql.substring(0, sql.length() - 1) + pageable.toString() + ";";
            }
        }
        return sql;
    }

    private static void showQuery(String query, Object... parameters) {
        if (PropertyConstants.getDatasourceShowSqlQuery()) {
            LOG.info("Executing SQL Query: {}", query);
            if (parameters != null && parameters.length > 0) {
                LOG.info("With parameters: {}", List.of(parameters));
            }
        }
    }

    public static void setDataSource(DataSource dataSource) {
        DB.dataSource = dataSource;
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

}
