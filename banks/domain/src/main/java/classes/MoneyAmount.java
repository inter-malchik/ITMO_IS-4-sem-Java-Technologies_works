package classes;

import exceptions.ValueException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Тип данных для количества денег.
 * Представляет значение в BigDecimal
 * Данный тип является иммутабельным, для изменения кол-ва денег создается новый объект
 *
 * @see BigDecimal
 */
public class MoneyAmount implements Comparable<MoneyAmount> {
  private final BigDecimal amount;

  /**
   * Создать объект по умолчанию.
   * Количество денег будет нулевым.
   */
  public MoneyAmount() {
    amount = BigDecimal.ZERO;
  }

  /**
   * Создать объект с определенным кол-вом денег
   *
   * @param amount желаемое кол-во, неотрицательное значение (валидируется)
   * @throws ValueException при получении отрицательного значения
   */
  public MoneyAmount(double amount) {
    if (amount < 0) {
      throw ValueException.NegativeMoneyAmount(amount);
    }
    this.amount = BigDecimal.valueOf(amount);
  }

  /**
   * Создать объект с определенным кол-вом денег
   *
   * @param amount желаемое кол-во, неотрицательное значение (валидируется)
   * @throws ValueException при получении отрицательного значения
   */
  public MoneyAmount(BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) < 0) {
      throw ValueException.NegativeMoneyAmount(amount.doubleValue());
    }
    this.amount = amount;
  }

  /**
   * @return записанное кол-во денег, переведенное в тип double из BigDecimal, округляется до 2-х знаков с отбрасыванием
   */
  public double getAmount() {
    return amount.setScale(2, RoundingMode.DOWN).doubleValue();
  }

  @Override
  public String toString() {
    return "MoneyAmount{" + "amount=" + getAmount() + '}';
  }

  @Override
  public int compareTo(MoneyAmount o) {
    return Double.compare(getAmount(), o.getAmount());
  }

  /**
   * Сравнить по внешнему значению (округленному)
   *
   * @param o второй объект
   * @return истина/ложь
   */
  public boolean isMoreThan(MoneyAmount o) {
    return getAmount() > o.getAmount();
  }

  /**
   * Сравнить по внешнему значению (округленному)
   *
   * @param o второй объект
   * @return истина/ложь
   */
  public boolean isLessThan(MoneyAmount o) {
    return getAmount() < o.getAmount();
  }

  /**
   * Сравнить по внешнему значению (округленному)
   *
   * @param o второй объект
   * @return истина/ложь
   */
  public boolean isEqualWith(MoneyAmount o) {
    return getAmount() == o.getAmount();
  }
}
