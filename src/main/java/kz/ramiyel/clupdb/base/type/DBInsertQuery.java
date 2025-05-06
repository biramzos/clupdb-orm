package kz.ramiyel.clupdb.base.type;

import kz.ramiyel.clupdb.base.DBQuery;
import kz.ramiyel.clupdb.enums.QueryType;
import kz.ramiyel.clupdb.exception.ParameterException;
import kz.ramiyel.clupdb.model.QueryColumn;
import kz.ramiyel.clupdb.model.QueryCondition;
import kz.ramiyel.clupdb.model.QueryTable;
import kz.ramiyel.clupdb.util.TableUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DBInsertQuery extends DBQuery {

    private final List<QueryColumn> columns = new ArrayList<>();
    private final List<List<Object>> values = new LinkedList<>();

    public DBInsertQuery(Class<?> clazz) {
        super(TableUtil.parseTable(clazz), QueryType.INSERT);
    }

    public DBInsertQuery columns(QueryColumn... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public DBInsertQuery value(Object... parameters) {
        if (parameters.length > columns.size()) {
            throw new ParameterException("Parameters are more than needed!");
        } else if (parameters.length < columns.size()) {
            throw new ParameterException("Parameters are less than needed!");
        }
        values.add(new LinkedList<>(Arrays.stream(parameters).toList()));
        return this;
    }

    public List<QueryColumn> getColumns() {
        return columns;
    }

    public List<List<Object>> getValues() {
        return values;
    }
}
