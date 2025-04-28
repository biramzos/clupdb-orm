package kz.ramiyel.clupdb.processor;

import kz.ramiyel.clupdb.model.DBPredicate;
import kz.ramiyel.clupdb.model.TableJoin;
import kz.ramiyel.clupdb.model.TableRoot;

public abstract class ConditionProcessor<T> {

    public abstract DBPredicate process(TableRoot<T> root, TableJoin... joins);

}
