package time;

import java.time.Duration;

/**
 * Интерфейс чувствительного ко времени объекта
 * Используется вместе с TimeService для реализации виртуальной перемотки времени
 * @see TimeService
 */
public interface ITimeSensitive {
  /**
   * Отреагировать на изменение времени
   * @param timeSpan зарегистрированное изменение времени
   */
  void skipTimePeriod(Duration timeSpan);
}
