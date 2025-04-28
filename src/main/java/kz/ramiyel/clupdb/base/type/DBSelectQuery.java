package kz.ramiyel.clupdb.base.type;

import kz.ramiyel.clupdb.base.DBQuery;
import kz.ramiyel.clupdb.model.DBCondition;
import kz.ramiyel.clupdb.model.QueryJoin;
import kz.ramiyel.clupdb.model.QueryOrderBy;
import kz.ramiyel.clupdb.model.QueryTable;
import kz.ramiyel.clupdb.model.QuerySelection;
import kz.ramiyel.clupdb.enums.QueryType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DBSelectQuery extends DBQuery {

    private List<QuerySelection> selections = new ArrayList<>();
    private List<QueryJoin> joins = new ArrayList<>();
    private List<DBCondition> conditions = new ArrayList<>();
    private List<String> groups = new ArrayList<>();
    private List<QueryOrderBy> orders = new ArrayList<>();
    private Integer limit = null;
    private Integer offset = null;

    public DBSelectQuery(QueryTable table) {
        super(table, QueryType.SELECT);
    }

    public DBSelectQuery select(QuerySelection... selections) {
        this.selections.addAll(Arrays.stream(selections).toList());
        return this;
    }

    public DBSelectQuery join(QueryJoin... joins) {
        this.joins.addAll(Arrays.stream(joins).toList());
        return this;
    }

    public DBSelectQuery where(DBCondition... conditions) {
        this.conditions.addAll(Arrays.stream(conditions).toList());
        return this;
    }

    public DBSelectQuery groupBy(String... columns) {
        this.groups.addAll(Arrays.stream(columns).toList());
        return this;
    }

    public DBSelectQuery orderBy(QueryOrderBy... orders) {
        this.orders.addAll(Arrays.stream(orders).toList());
        return this;
    }

    public DBSelectQuery limit(Integer limit) {
        if (Objects.nonNull(limit)) {
            this.limit = limit;
        } else {
            this.limit = null;
        }
        return this;
    }

    public DBQuery offset(Integer offset) {
        if (Objects.nonNull(offset)) {
            this.offset = offset;
        } else {
            this.offset = null;
        }
        return this;
    }

    public List<QuerySelection> getSelections() {
        return selections;
    }

    public List<QueryJoin> getJoins() {
        return joins;
    }

    public List<DBCondition> getConditions() {
        return conditions;
    }

    public List<String> getGroups() {
        return groups;
    }

    public List<QueryOrderBy> getOrders() {
        return orders;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return offset;
    }
}
