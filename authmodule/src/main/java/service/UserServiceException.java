package service;

public class UserServiceException extends RuntimeException {
    public UserServiceException(String message) {
        super(message);
    }

    public UserServiceException(String message, Throwable inner) {
        super(message, inner);
    }

    public static UserServiceException UserAlreadyPresented(String username) {
        return new UserServiceException(String.format("user " + username + " already presented in the database"));
    }
}
