package microservices;

public class CatsMicroHandlerException extends RuntimeException {

    public CatsMicroHandlerException(String message) {
        super(message);
    }

    public CatsMicroHandlerException(String message, Throwable inner) {
        super(message, inner);
    }

    public static CatsMicroHandlerException CannotGetCat(int id) {
        return new CatsMicroHandlerException(String.format("kitten (%b) is not present", id));
    }
}
