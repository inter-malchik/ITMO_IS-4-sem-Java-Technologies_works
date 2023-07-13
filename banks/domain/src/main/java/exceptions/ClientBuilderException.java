package exceptions;

public class ClientBuilderException extends BanksBaseException {
  private ClientBuilderException(String message) {
    super(message);
  }

  public static ClientBuilderException NoNameSpecified() {
    return new ClientBuilderException("no name was specified when constructing client");
  }

  public static ClientBuilderException NoSurnameSpecified() {
    return new ClientBuilderException("no surname was specified when constructing client");
  }
}
