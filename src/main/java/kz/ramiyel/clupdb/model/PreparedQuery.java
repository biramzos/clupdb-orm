package kz.ramiyel.clupdb.model;

public class PreparedQuery {
    private String query;
    private Object[] parameters;

    public PreparedQuery(String query, Object... parameters) {
        this.query = query;
        this.parameters = parameters;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object... parameters) {
        this.parameters = parameters;
    }
}
