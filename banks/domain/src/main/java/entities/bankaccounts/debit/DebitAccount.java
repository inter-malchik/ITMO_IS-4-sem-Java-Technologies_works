package entities.bankaccounts.debit;

import classes.MoneyAmount;
import entities.bankaccounts.IBankAccount;
import entities.banks.IBank;
import entities.clients.IClient;
import exceptions.AccountException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Класс для депозитарного счета
 * Каждый день начисляются проценты на остаток,
 * которые добавляются к общему балансу в конце месяца
 * @see DebitTerms
 */
public class DebitAccount implements IBankAccount {
  private final List<Double> interestCalculations;
  private double money;
  private final DebitTerms terms;
  private final UUID id;
  private final IClient client;
  private final IBank bank;

  /**
   * Инстанцировать дебетный счет
   *
   * @param terms  условия счета
   * @param client владелец счета
   * @param bank   управляющий банк
   */
  public DebitAccount(DebitTerms terms, IClient client, IBank bank) {
    this.money = 0;
    this.terms = terms;
    this.interestCalculations = new ArrayList<>();
    this.id = UUID.randomUUID();
    this.client = client;
    this.bank = bank;
    this.bank.getCentralBank().getTimeService().subscribe(this);
  }

  @Override
  public MoneyAmount getAvailableMoney() {
    return new MoneyAmount(money);
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public IClient getClient() {
    return client;
  }

  @Override
  public IBank getBank() {
    return bank;
  }

  @Override
  public void payIn(MoneyAmount amount) {
    money += amount.getAmount();
  }

  @Override
  public void withdraw(MoneyAmount amount) {
    if (money < amount.getAmount()) {
      throw AccountException.NotEnoughMoneyException(amount, getAvailableMoney());
    }
    money -= amount.getAmount();
  }

  private void HandleNextDay() {
    interestCalculations.add(
            terms.getBalanceInterest().getRate() / 365 * getAvailableMoney().getAmount());
    if (interestCalculations.size() == 30) {
      money += interestCalculations.stream().mapToDouble(Double::doubleValue).sum();
      interestCalculations.clear();
    }
  }

  @Override
  public void skipTimePeriod(Duration timeSpan) {
    for (long day = 0; day < timeSpan.toDays(); day++) {
      HandleNextDay();
    }
  }
}
