package exceptions;

import entities.bankaccounts.deposit.DepositAccount;

public class DepositException extends BanksBaseException {
  private DepositException(String message) {

    super(message);
  }

  public static DepositException DepositHasNotExpired(DepositAccount deposit) {
    return new DepositException(String.format("deposit (%b) has not expired", deposit.getId()));
  }

  public static DepositException TimeSpanNotSpecified() {
    return new DepositException("time span not specified");
  }
}
