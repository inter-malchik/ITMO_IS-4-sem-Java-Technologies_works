package entities.bankaccounts.credit;

import classes.MoneyAmount;
import entities.bankaccounts.IBankAccount;
import entities.banks.IBank;
import entities.clients.IClient;
import exceptions.CreditException;

import java.time.Duration;
import java.util.UUID;

/**
 * Класс для кредитного счета
 * Позволяет уходить в минус в пределах кредитного лимита
 * Имеет фиксированную комиссию за использование кредитного лимита
 * @see CreditTerms
 */
public class CreditAccount implements IBankAccount {
  private double balance;
  private final CreditTerms terms;
  private final UUID id;

  private final IClient client;
  private final IBank bank;

  /**
   * Инстанцировать кредитный счет
   *
   * @param terms  условия счета
   * @param client владелец счета
   * @param bank   управляющий банк
   */
  public CreditAccount(CreditTerms terms, IClient client, IBank bank) {
    this.terms = terms;
    this.balance = 0;
    this.id = UUID.randomUUID();
    this.client = client;
    this.bank = bank;
  }

  @Override
  public MoneyAmount getAvailableMoney() {
    return new MoneyAmount(terms.getCreditLimit().getAmount() + balance);
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

  /**
   * @return кредитный лимит (в соответствии с условиями счета)
   */
  public MoneyAmount getCreditLimit() {
    return terms.getCreditLimit();
  }

  @Override
  public void payIn(MoneyAmount amount) {
    balance += amount.getAmount();
  }

  @Override
  public void withdraw(MoneyAmount amount) {
    if (-amount.getAmount() < balance) {
      throw CreditException.TryingToExceedCreditLimit(this);
    }
    balance = balance - amount.getAmount() - terms.getLimitFee().getAmount();
  }

  @Override
  public void skipTimePeriod(Duration timeSpan) {
  }
}
