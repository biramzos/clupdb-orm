package kz.ramiyel.clupdb.base.type;

import kz.ramiyel.clupdb.base.DBQuery;
import kz.ramiyel.clupdb.model.DBCondition;
import kz.ramiyel.clupdb.model.QueryJoin;
import kz.ramiyel.clupdb.model.QueryOrderBy;
import kz.ramiyel.clupdb.model.QuerySelection;
import kz.ramiyel.clupdb.enums.QueryType;
import kz.ramiyel.clupdb.model.TableJoin;
import kz.ramiyel.clupdb.model.TableRoot;
import kz.ramiyel.clupdb.processor.ConditionProcessor;
import kz.ramiyel.clupdb.processor.ExtendedConditionProcessor;
import kz.ramiyel.clupdb.util.TableUtil;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class DBSelectQuery extends DBQuery {

    private List<QuerySelection> selections = new LinkedList<>();
    private List<QueryJoin> joins = new LinkedList<>();
    private List<DBCondition> conditions = new LinkedList<>();
    private List<QuerySelection> groups = new LinkedList<>();
    private List<DBCondition> havings = new LinkedList<>();
    private List<QueryOrderBy> orders = new LinkedList<>();
    private Integer limit = null;
    private Integer offset = null;

    public DBSelectQuery(Class<?> clazz) {
        super(TableUtil.parseTable(clazz), QueryType.SELECT);
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

    public DBSelectQuery where(TableRoot root, ConditionProcessor processor) {
        return where(processor.process(root, mapJoins()));
    }

    public DBSelectQuery groupBy(QuerySelection... columns) {
        this.groups.addAll(Arrays.stream(columns).toList());
        return this;
    }

    public DBSelectQuery having(DBCondition... conditions) {
        this.havings.addAll(Arrays.stream(conditions).toList());
        return this;
    }

    public DBSelectQuery having(TableRoot root, ExtendedConditionProcessor processor) {
        return having(processor.process(root, mapJoins()));
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

    public List<QuerySelection> getGroups() {
        return groups;
    }

    public List<DBCondition> getHavings() {
        return havings;
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

    private TableJoin[] mapJoins() {
        return this.joins.stream().map(join -> new TableJoin<>(join.getFromClass(), join.getJoinClass())).toArray(TableJoin[]::new);
    }
}
