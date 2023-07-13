package entities.clients;

import java.util.UUID;

/**
 * Интерфейс клиента банка
 */
public interface IClient {
  /**
   * @return имя
   */
  String getName();

  /**
   * @return фамилия
   */
  String getSurname();

  /**
   * @return адрес (опционально)
   */
  String getAddress();

  /**
   * @return номер паспорта (опционально)
   */
  String getPassportNumber();

  /**
   * Является ли клиент сомнительным
   * наследники имеют право переопределять критерий на их усмотрение
   * @return истина/ложь
   */
  boolean isDoubtful();

  /**
   * @return id объекта
   */
  UUID getId();
}
