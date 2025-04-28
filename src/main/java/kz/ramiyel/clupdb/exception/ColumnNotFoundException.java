package kz.ramiyel.clupdb.exception;

public class ColumnNotFoundException extends RuntimeException {

    public ColumnNotFoundException() {
        super("Column not found");
    }
}
