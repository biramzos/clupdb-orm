package kz.ramiyel.clupdb.extractor.type;

import kz.ramiyel.clupdb.extractor.ResultSetExtractor;
import kz.ramiyel.clupdb.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListRowExtractor<T> implements ResultSetExtractor<List<T>> {

    private final RowMapper<T> mapper;

    public ListRowExtractor(RowMapper<T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<T> extractData(ResultSet resultSet) throws SQLException {
        List<T> list = new ArrayList<>();
        while(resultSet.next()) list.add(mapper.mapRow(resultSet));
        return list;
    }
}
