package exceptions;

import classes.MoneyAmount;

public class AccountException extends BanksBaseException {
  private AccountException(String message) {
    super(message);
  }

  public static AccountException NotEnoughMoneyException(
      MoneyAmount withdrawn, MoneyAmount actual) {
    return new AccountException(String.format("trying to withdraw %b with %b", withdrawn, actual));
  }
}
