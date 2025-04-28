package kz.ramiyel.clupdb.exception;

public class UnknownQueryTypeException extends RuntimeException {
    public UnknownQueryTypeException() {
        super("Unknown query type");
    }
}
