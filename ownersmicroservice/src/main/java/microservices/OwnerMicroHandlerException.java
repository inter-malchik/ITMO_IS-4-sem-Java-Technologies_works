package microservices;

public class OwnerMicroHandlerException extends RuntimeException {

    public OwnerMicroHandlerException(String message) {
        super(message);
    }

    public OwnerMicroHandlerException(String message, Throwable inner) {
        super(message, inner);
    }

    public static OwnerMicroHandlerException CannotGetOwner(int id) {
        return new OwnerMicroHandlerException(String.format("Owner (%b) is not present", id));
    }
}
