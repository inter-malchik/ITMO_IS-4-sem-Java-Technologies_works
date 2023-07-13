package exceptions;

public class BanksBaseException extends RuntimeException {
  public BanksBaseException() {}

  public BanksBaseException(String message) {
    super(message);
  }

  public BanksBaseException(String message, Throwable inner) {
    super(message, inner);
  }
}
