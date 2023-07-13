package classes;

import exceptions.ValueException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Тип данных для процентной ставки
 * Представляет значение в BigDecimal
 * Данный тип является иммутабельным, для изменения значения ставки создается новый объект
 */
public class PercentRate {
  private final BigDecimal rate;

  /**
   * Создать объект по умолчанию.
   * Ставка будет нулевая
   */
  public PercentRate() {
    rate = BigDecimal.ZERO;
  }

  /**
   * Создать объект с определенной процентной ставкой
   * Ставка может передаваться как в диапазоне [0;1] так и в [0;100]
   * под капотом превращается в uniform - значение
   * @param rate желаемое кол-во, неотрицательное значение, не превышает 100 (валидируется)
   * @throws ValueException при получении невалидного значения
   */
  public PercentRate(double rate) {
    if (rate < 0 || rate > 100) {
      throw ValueException.IncorrectRateValue(rate);
    }

    this.rate = BigDecimal.valueOf((rate > 1) ? rate / 100 : rate);
  }

  /**
   * @return процентная ставка, переведенная в тип double из BigDecimal, округляется до 2-х знаков с отбрасыванием
   */
  public double getRate() {
    return rate.setScale(2, RoundingMode.DOWN).doubleValue();
  }

  @Override
  public String toString() {
    return "PercentRate{" + "rate=" + getRate() + '}';
  }
}
