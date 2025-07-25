package kz.ramiyel.clupdb.extractor.type;

import kz.ramiyel.clupdb.constants.BasicColumnConstants;
import kz.ramiyel.clupdb.dtos.TPage;
import kz.ramiyel.clupdb.extractor.ResultSetExtractor;
import kz.ramiyel.clupdb.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PageRowExtractor<T> implements ResultSetExtractor<TPage<T>> {
    private final RowMapper<T> rowMapper;

    public PageRowExtractor(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public TPage<T> extractData(ResultSet rs) throws SQLException {
        List<T> results = new ArrayList<>();
        long total = 0;
        while (rs.next()) {
            T row = rowMapper.mapRow(rs);
            results.add(row);
            if (total == 0) {
                total = rs.getLong(BasicColumnConstants.TOTAL_COLUMN_NAME);
            }
        }
        return new TPage<>(results, total);
    }
}
