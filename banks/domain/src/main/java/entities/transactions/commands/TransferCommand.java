package entities.transactions.commands;

import classes.MoneyAmount;
import entities.bankaccounts.IBankAccount;
import exceptions.TransactionsException;

/**
 * The type Transfer command.
 */
public class TransferCommand implements ICommand {
  private final IBankAccount accountFrom;
  private final IBankAccount accountTo;
  private final MoneyAmount amount;

  /**
   * Instantiates a new Transfer command.
   *
   * @param accountFrom the account from
   * @param accountTo   the account to
   * @param amount      the amount
   */
  public TransferCommand(IBankAccount accountFrom, IBankAccount accountTo, MoneyAmount amount) {
    this.amount = amount;

    if (accountFrom.getId() == accountTo.getId()) {
      throw TransactionsException.TransferToTheSameAccount(accountFrom);
    }
    this.accountFrom = accountFrom;
    this.accountTo = accountTo;
  }

  @Override
  public void execute() {
    accountFrom.withdraw(amount);
    accountTo.payIn(amount);
  }

  @Override
  public void cancel() {
    accountFrom.payIn(amount);
    accountTo.withdraw(amount);
  }
}
