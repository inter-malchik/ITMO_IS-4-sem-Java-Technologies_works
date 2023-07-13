package entities.bankaccounts.credit;

import classes.MoneyAmount;

/**
 * Класс условий для дебетного счета.
 * Предполагается, что будет создан только 1 класс условий для множества счетов.
 * Реализует поведение по типу легковеса
 */
public class CreditTerms {
  private MoneyAmount limitFee;
  private MoneyAmount creditLimit;

  /**
   * @param limitFee    комиссия за использование кредитного лимита
   * @param creditLimit кредитный лимит
   */
  public CreditTerms(MoneyAmount limitFee, MoneyAmount creditLimit) {
    this.limitFee = limitFee;
    this.creditLimit = creditLimit;
  }

  /**
   * @return комиссия за использование кредитного лимита
   */
  public MoneyAmount getLimitFee() {
    return limitFee;
  }

  /**
   * @return кредитный лимит
   */
  public MoneyAmount getCreditLimit() {
    return creditLimit;
  }

  /**
   * Обновление условий счета
   * копирует данные другого объета, сохраняя инстанс
   * используется доя того, чтобы динамически менять условия у существующих счетов
   * @param other другой объект условий
   */
  public void update(CreditTerms other) {
    limitFee = other.limitFee;
    creditLimit = other.creditLimit;
  }
}
