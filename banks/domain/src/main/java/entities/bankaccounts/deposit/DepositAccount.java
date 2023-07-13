package entities.bankaccounts.deposit;

import classes.MoneyAmount;
import entities.bankaccounts.IBankAccount;
import entities.banks.IBank;
import entities.clients.IClient;
import exceptions.AccountException;
import exceptions.DepositException;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Класс для депозитарного счета
 * Создается на конкретное время, каждый день начисляются проценты на остаток,
 * которые добавляются к общему балансу в конце месяца.
 * Запрещается снятия до окончания срока действия
 * @see DepositTerms
 */
public class DepositAccount implements IBankAccount {
  private final List<Double> interestCalculations;
  private double money;
  private final DepositTerms terms;
  private final UUID id;
  private final IClient client;
  private final IBank bank;
  private final LocalDate openingDate;
  private final LocalDate closingDate;

  /**
   * Инстанцировать депозитарный счет
   *
   * @param terms  условия счета
   * @param client владелец счета
   * @param bank   управляющий банк
   */
  public DepositAccount(DepositTerms terms, IClient client, IBank bank) {
    this.money = 0;
    this.terms = terms;
    this.interestCalculations = new ArrayList<>();
    this.id = UUID.randomUUID();
    this.client = client;
    this.bank = bank;
    this.bank.getCentralBank().getTimeService().subscribe(this);
    this.openingDate = this.bank.getCentralBank().getTimeService().getDate();
    this.closingDate = this.openingDate.plusDays(terms.getTimePeriod().toDays());
  }

  /**
   * @return условия депозита
   */
  public DepositTerms getTerms() {
    return terms;
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
    if (this.bank.getCentralBank().getTimeService().getDate().isBefore(closingDate)) {
      throw DepositException.DepositHasNotExpired(this);
    }
    if (money < amount.getAmount()) {
      throw AccountException.NotEnoughMoneyException(amount, getAvailableMoney());
    }
    money -= amount.getAmount();
  }

  private void HandleNextDay() {
    double balanceInterest =
            terms.getBalanceInterests().entrySet().stream()
                    .filter(entry -> entry.getKey().getAmount() > money)
                    .mapToDouble(entry -> entry.getValue().getRate())
                    .max()
                    .getAsDouble();
    interestCalculations.add(balanceInterest / 365 * getAvailableMoney().getAmount());
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
