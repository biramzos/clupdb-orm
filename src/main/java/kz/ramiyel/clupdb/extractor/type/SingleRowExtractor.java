package kz.ramiyel.clupdb.extractor.type;

import kz.ramiyel.clupdb.extractor.ResultSetExtractor;
import kz.ramiyel.clupdb.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SingleRowExtractor<T> implements ResultSetExtractor<T> {

    private final RowMapper<T> mapper;

    public SingleRowExtractor(RowMapper<T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public T extractData(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) return mapper.mapRow(resultSet);
        return null;
    }
}
