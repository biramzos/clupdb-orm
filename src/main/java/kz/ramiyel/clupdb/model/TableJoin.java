package kz.ramiyel.clupdb.model;

public class TableJoin<F, J> extends TableRoot<J> {
    private final Class<F> fromClazz;

    public TableJoin(Class<F> fromClazz, Class<J> joinClass) {
        super(joinClass);
        this.fromClazz = fromClazz;
    }

    public Class<F> getFromClazz() {
        return fromClazz;
    }
}
