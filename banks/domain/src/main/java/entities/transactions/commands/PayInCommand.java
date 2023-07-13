package entities.transactions.commands;

import classes.MoneyAmount;
import entities.bankaccounts.IBankAccount;

/**
 * Банковская команда пополнения
 * Зачисляет деньги на конкретный счет
 */
public class PayInCommand implements ICommand {
  private final IBankAccount account;
  private final MoneyAmount amount;

  /**
   * @param account банковский счет
   * @param amount  сумма пополнения
   */
  public PayInCommand(IBankAccount account, MoneyAmount amount) {
    this.account = account;
    this.amount = amount;
  }

  @Override
  public void execute() {
    account.payIn(amount);
  }

  @Override
  public void cancel() {
    account.withdraw(amount);
  }
}
