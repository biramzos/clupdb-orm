package kz.ramiyel.clupdb.model;

import kz.ramiyel.clupdb.enums.OrderType;

public class QueryOrderBy {
    private String column;
    private OrderType type;

    public QueryOrderBy(String column) {
        this(column, OrderType.ASC);
    }

    public QueryOrderBy(String column, OrderType type) {
        this.column = column;
        this.type = type;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public String toSql() {
        return column + " " + type.getSql();
    }
}
