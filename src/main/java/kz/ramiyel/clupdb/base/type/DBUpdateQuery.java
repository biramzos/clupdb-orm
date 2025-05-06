package kz.ramiyel.clupdb.base.type;

import kz.ramiyel.clupdb.base.DBQuery;
import kz.ramiyel.clupdb.enums.QueryType;
import kz.ramiyel.clupdb.model.DBCondition;
import kz.ramiyel.clupdb.model.QuerySelection;
import kz.ramiyel.clupdb.model.QuerySelectionValue;
import kz.ramiyel.clupdb.model.QueryTable;
import kz.ramiyel.clupdb.util.TableUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBUpdateQuery extends DBQuery {

    private List<QuerySelectionValue> selections = new ArrayList<>();
    private List<DBCondition> conditions = new ArrayList<>();

    public DBUpdateQuery(Class<?> clazz) {
        super(TableUtil.parseTable(clazz), QueryType.UPDATE);
    }

    public DBUpdateQuery set(QuerySelectionValue... selections) {
        this.selections.addAll(Arrays.asList(selections));
        return this;
    }

    public DBUpdateQuery where(DBCondition... conditions) {
        this.conditions.addAll(Arrays.asList(conditions));
        return this;
    }

    public List<QuerySelectionValue> getSelections() {
        return selections;
    }

    public List<DBCondition> getConditions() {
        return conditions;
    }
}
