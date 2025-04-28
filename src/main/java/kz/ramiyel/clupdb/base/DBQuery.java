package kz.ramiyel.clupdb.base;

import kz.ramiyel.clupdb.model.QueryTable;
import kz.ramiyel.clupdb.enums.QueryType;

public abstract class DBQuery {
    private final QueryTable table;
    private final QueryType queryType;

    public DBQuery(QueryTable table, QueryType queryType) {
        this.table = table;
        this.queryType = queryType;
    }

    public QueryTable getTable() {
        return table;
    }

    public QueryType getQueryType() {
        return queryType;
    }
}
