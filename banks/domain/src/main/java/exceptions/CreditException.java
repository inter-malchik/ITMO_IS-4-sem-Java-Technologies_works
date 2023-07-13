package exceptions;

import entities.bankaccounts.credit.CreditAccount;

public class CreditException extends BanksBaseException {
  private CreditException(String message) {

    super(message);
  }

  public static CreditException TryingToExceedCreditLimit(CreditAccount account) {
    return new CreditException(
        String.format("trying to withdraw below the credit limit - %b", account.getCreditLimit()));
  }
}
