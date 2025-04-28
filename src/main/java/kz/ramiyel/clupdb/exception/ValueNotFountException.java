package kz.ramiyel.clupdb.exception;

public class ValueNotFountException extends RuntimeException{

    public ValueNotFountException() {
        super("Value not found");
    }
}
