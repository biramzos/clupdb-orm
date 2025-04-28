package kz.ramiyel.clupdb.exception;

public class TableNotFoundException extends RuntimeException{
    public TableNotFoundException() {
        super("Table not found");
    }
}
