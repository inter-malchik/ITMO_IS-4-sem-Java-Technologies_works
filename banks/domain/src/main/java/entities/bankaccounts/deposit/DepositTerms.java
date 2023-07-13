package entities.bankaccounts.deposit;

import classes.MoneyAmount;
import classes.PercentRate;
import exceptions.DepositException;

import java.time.Duration;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Класс условий депозитарного счета.
 * Предполагается, что будет создан только 1 класс условий для множества счетов.
 * Реализует поведение по типу легковеса
 */
public class DepositTerms {
  private SortedMap<MoneyAmount, PercentRate> balanceInterests;
  private Duration timePeriod;

  private DepositTerms(SortedMap<MoneyAmount, PercentRate> balanceInterests, Duration timePeriod) {
    this.balanceInterests = balanceInterests;
    this.timePeriod = timePeriod;
  }

  /**
   * @return процентные ставки в зависимости от баланса счета
   */
  public SortedMap<MoneyAmount, PercentRate> getBalanceInterests() {
    return Collections.unmodifiableSortedMap(balanceInterests);
  }

  /**
   * @return длительность депозита
   */
  public Duration getTimePeriod() {
    return timePeriod;
  }

  /**
   * Обновление условий счета
   * копирует данные другого объета, сохраняя инстанс
   * используется доя того, чтобы динамически менять условия у существующих счетов
   *
   * @param other другой объект условий
   */
  public void update(DepositTerms other) {
    balanceInterests = other.balanceInterests;
    timePeriod = other.timePeriod;
  }

  /**
   * @return класс-строитель депозитарного счета
   */
  public static DepositTermsBuilder getBuilder() {
    return new DepositTermsBuilder();
  }

  /**
   * класс-строитель депозитарного счета
   */
  public static class DepositTermsBuilder {
    private final TreeMap<MoneyAmount, PercentRate> balanceInterests;
    private Duration timeSpan;

    /**
     * Конструктор строителя
     */
    public DepositTermsBuilder() {
      balanceInterests = new TreeMap<MoneyAmount, PercentRate>();
    }

    /**
     * Добавить процентную ставку в соответствии с балансом счета
     *
     * @param bound граница баланса
     * @param rate  процентная ставка
     * @return объект строителя
     */
    public DepositTermsBuilder addInterestBound(MoneyAmount bound, PercentRate rate) {
      balanceInterests.put(bound, rate);
      return this;
    }

    /**
     * @param timeSpan длительность депозитного счета
     * @return объект строителя
     */
    public DepositTermsBuilder setTimeSpan(Duration timeSpan) {
      this.timeSpan = timeSpan;
      return this;
    }

    /**
     * @return Сконструированный объект
     * @throws DepositException Проверяется существование и корректность длительности депозита
     */
    public DepositTerms build() {
      if (timeSpan == null || timeSpan.isNegative() || timeSpan.isZero()) {
        throw DepositException.TimeSpanNotSpecified();
      }
      return new DepositTerms(balanceInterests, timeSpan);
    }
  }

  /**
   * Вспомогательный класс для заготовленных длительностей депозитп
   */
  public static class DepositTimeSpans {
    /**
     * @return 1 месяц (30 дней)
     */
    public static Duration oneMonth() {
      return Duration.ofDays(30);
    }

    /**
     * @return 3 месяца (90 дней)
     */
    public static Duration threeMonth() {
      return Duration.ofDays(90);
    }

    /**
     * @return 6 месяцев (180 дней)
     */
    public static Duration sixMonth() {
      return Duration.ofDays(180);
    }

    /**
     * Параметризуемый срок в месяцах
     *
     * @param amount кол-во месяцев
     * @return n месяцев (n*30 дней)
     */
    public static Duration months(int amount) {
      return Duration.ofDays(amount * 30L);
    }
  }
}
