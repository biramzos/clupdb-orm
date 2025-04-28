package kz.ramiyel.clupdb.base.type;

import kz.ramiyel.clupdb.base.DBQuery;
import kz.ramiyel.clupdb.enums.QueryType;
import kz.ramiyel.clupdb.model.DBCondition;
import kz.ramiyel.clupdb.model.QueryTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBDeleteQuery extends DBQuery {
    private List<DBCondition> conditions = new ArrayList<>();
    public DBDeleteQuery(QueryTable table) {
        super(table, QueryType.DELETE);
    }

    public DBDeleteQuery where(DBCondition... conditions) {
        this.conditions.addAll(Arrays.asList(conditions));
        return this;
    }

    public List<DBCondition> getConditions() {
        return conditions;
    }

}
