package time;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для управления виртуальным (доменным) временем
 * Предоставляет функционал подписки на изменения времени,
 * при регистрации изменения времени оповещает об этом чувствительные объекты
 * @see ITimeSensitive
 */
public class TimeService {
  private final List<ITimeSensitive> subscribers;
  private LocalDateTime dateTime;

  /**
   * Отсчет идет от времени создания объекта
   */
  public TimeService() {
    subscribers = new ArrayList<>();
    dateTime = LocalDateTime.now();
  }

  /**
   * Позволяет получить актуальную дату и время сервиса
   *
   * @return дату и время
   */
  public LocalDateTime getDateTime() {
    return dateTime;
  }

  /**
   * Позволяет получить актуальную дату сервиса
   *
   * @return только дату
   */
  public LocalDate getDate() {
    return dateTime.toLocalDate();
  }

  /**
   * Отслеживать чувствительный ко времени объект
   *
   * @param sensitiveObject объект реализующий интерфейс ITimeSensitive
   */
  public void subscribe(ITimeSensitive sensitiveObject) {
    subscribers.add(sensitiveObject);
  }

  /**
   * Зарегистрировать изменение времени
   * При регистрации изменения времени об этом факте оповещаются все отслеживаемые объекты
   *
   * @param timeSpan зарегистрированное изменение времени (точность: сок)
   */
  public void registerTimeDifference(Duration timeSpan) {
    dateTime = dateTime.plusSeconds(timeSpan.getSeconds());

    for (ITimeSensitive subscriber : subscribers) {
      subscriber.skipTimePeriod(timeSpan);
    }
  }
}
