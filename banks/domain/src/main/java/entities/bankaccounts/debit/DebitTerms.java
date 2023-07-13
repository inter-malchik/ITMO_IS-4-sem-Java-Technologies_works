package entities.bankaccounts.debit;

import classes.PercentRate;

/**
 * Класс условий для дебетного счета.
 * Предполагается, что будет создан только 1 класс условий для множества счетов.
 * Реализует поведение по типу легковеса
 */
public class DebitTerms {
  private PercentRate balanceInterest;

  /**
   * @param balanceInterest процентная ставка на остаток
   */
  public DebitTerms(PercentRate balanceInterest) {
    this.balanceInterest = balanceInterest;
  }

  /**
   * @return процентная ставка
   */
  PercentRate getBalanceInterest() {
    return balanceInterest;
  }

  /**
   * Обновление условий счета
   * копирует данные другого объета, сохраняя инстанс
   * используется доя того, чтобы динамически менять условия у существующих счетов
   * @param other другой объект условий
   */
  public void update(DebitTerms other) {
    balanceInterest = other.balanceInterest;
  }
}
