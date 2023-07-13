package exceptions;

public class ValueException extends BanksBaseException {
  private ValueException(String message) {
    super(message);
  }

  public static ValueException NegativeMoneyAmount(double amount) {
    return new ValueException(String.format("negative money amount: %.2f", amount));
  }

  public static ValueException IncorrectRateValue(double rate) {
    return new ValueException(
        String.format("incorrect rate value: %.2f - not in [0; 1] or [0; 100]", rate));
  }
}
