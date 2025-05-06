package kz.ramiyel.clupdb.model;

import java.util.ArrayList;
import java.util.List;

public abstract class DBCondition {
    private final List<Object> parameters = new ArrayList<>();
    public abstract String toSql();

    public List<Object> getParameters() {
        return parameters;
    }
}
