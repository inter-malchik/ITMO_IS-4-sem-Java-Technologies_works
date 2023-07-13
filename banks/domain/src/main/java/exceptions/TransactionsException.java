package exceptions;

import entities.bankaccounts.IBankAccount;
import entities.transactions.info.ICommandInfo;

import java.util.UUID;

public class TransactionsException extends BanksBaseException {
  private TransactionsException(String message) {

    super(message);
  }

  public static TransactionsException AlreadyPerformed(ICommandInfo commandInfo) {
    return new TransactionsException(
        String.format("command (%b) already performed", commandInfo.getId()));
  }

  public static TransactionsException HasNotBeenPerformed(ICommandInfo commandInfo) {
    return new TransactionsException(
        String.format("command (%b) has not been performed", commandInfo.getId()));
  }

  public static TransactionsException AlreadyCanceled(ICommandInfo commandInfo) {
    return new TransactionsException(
        String.format("command (%b) already canceled", commandInfo.getId()));
  }

  public static TransactionsException TransferToTheSameAccount(IBankAccount account) {
    return new TransactionsException(
        String.format("transfering to the same account (%b)", account.getId()));
  }

  public static TransactionsException CommandNotFound(UUID id) {
    return new TransactionsException(String.format("command (%b) not found", id));
  }
}
