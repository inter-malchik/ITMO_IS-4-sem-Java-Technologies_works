package exceptions;

import entities.bankaccounts.IBankAccount;
import entities.banks.IBank;

import java.util.UUID;

public class CentralBankException extends BanksBaseException {
  private CentralBankException(String message) {
    super(message);
  }

  public static CentralBankException BankNotRegistered(IBank bank) {
    return new CentralBankException(String.format("bank (%b) is not registered", bank.getId()));
  }

  public static CentralBankException BankNotRegistered(UUID id) {
    return new CentralBankException(String.format("bank (%b) is not registered", id));
  }

  public static CentralBankException BankAlreadyRegistered(IBank bank) {
    return new CentralBankException(
        String.format("bank (%b) has already registered", bank.getId()));
  }

  public static CentralBankException TryingToDoDomesticTransfer(
      IBankAccount from, IBankAccount to) {
    return new CentralBankException(
        String.format(
                "accounts (%b) and (%b) are from the same bank (%b)\n",
                from.getId(), to.getId(), from.getBank().getId())
            + "(consider using domestic transfer service)");
  }
}
