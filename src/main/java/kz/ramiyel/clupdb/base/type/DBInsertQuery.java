package kz.ramiyel.clupdb.base.type;

import kz.ramiyel.clupdb.base.DBQuery;
import kz.ramiyel.clupdb.enums.QueryType;
import kz.ramiyel.clupdb.model.QueryColumn;
import kz.ramiyel.clupdb.model.QueryCondition;
import kz.ramiyel.clupdb.model.QueryTable;
import kz.ramiyel.clupdb.util.TableUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBInsertQuery extends DBQuery {

    private final List<QueryColumn> columns = new ArrayList<>();

    public DBInsertQuery(Class<?> clazz) {
        super(TableUtil.parseTable(clazz), QueryType.INSERT);
    }

    public DBInsertQuery columns(QueryColumn... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public List<QueryColumn> getColumns() {
        return columns;
    }

}
