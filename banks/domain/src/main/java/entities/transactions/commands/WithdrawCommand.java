package entities.transactions.commands;

import classes.MoneyAmount;
import entities.bankaccounts.IBankAccount;

/**
 * Банковская команда снятия средств
 * Изымает деньги с конкретного счета
 */
public class WithdrawCommand implements ICommand {
  private final IBankAccount account;
  private final MoneyAmount amount;

  /**
   * @param account банковский счет
   * @param amount  сумма изъятия
   */
  public WithdrawCommand(IBankAccount account, MoneyAmount amount) {
    this.account = account;
    this.amount = amount;
  }

  @Override
  public void execute() {
    account.withdraw(amount);
  }

  @Override
  public void cancel() {
    account.payIn(amount);
  }
}
