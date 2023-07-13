package services.exceptions;

public class CatsBLLException extends RuntimeException {
    public CatsBLLException() {
    }

    public CatsBLLException(String message) {
        super(message);
    }

    public CatsBLLException(String message, Throwable inner) {
        super(message, inner);
    }

    public static CatsBLLException messageWasNotProcessed() {
        return new CatsBLLException("error occured in processing message");
    }

    public static CatsBLLException errorInMessageOccured() {
        return new CatsBLLException("error occured in completing message request");
    }

    public static CatsBLLException nullMessage() {
        return new CatsBLLException("null message received from broker");
    }
}
