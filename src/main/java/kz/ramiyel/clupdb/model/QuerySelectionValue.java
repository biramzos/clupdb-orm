package kz.ramiyel.clupdb.model;

public class QuerySelectionValue {

    private final QuerySelection selection;
    private final Object value;

    public QuerySelectionValue(QuerySelection selection, Object value) {
        this.selection = selection;
        this.value = value;
    }

    public QuerySelectionValue(QuerySelection selection, QueryExpression value) {
        this.selection = selection;
        this.value = value;
    }

    public QuerySelectionValue(QuerySelection selection, QuerySelection value) {
        this.selection = selection;
        this.value = value;
    }

    public static QuerySelectionValue of(QuerySelection selection, Object value) {
        return new QuerySelectionValue(selection, value);
    }

    public static QuerySelectionValue of(QuerySelection selection, QueryExpression value) {
        return new QuerySelectionValue(selection, value);
    }

    public static QuerySelectionValue of(QuerySelection selection, QuerySelection value) {
        return new QuerySelectionValue(selection, value);
    }

    public QuerySelection getSelection() {
        return selection;
    }

    public Object getValue() {
        return value;
    }
}
