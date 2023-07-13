package entities.bankaccounts;

import classes.MoneyAmount;
import entities.banks.IBank;
import entities.clients.IClient;
import time.ITimeSensitive;

import java.util.UUID;

/**
 * Интерфейс счета в банке
 * инкапсулирует функционал взаимодействия с банковским счетом
 */
public interface IBankAccount extends ITimeSensitive {
  /**
   * @return доступное кол-во денег
   */
  MoneyAmount getAvailableMoney();

  /**
   * @return id объекта
   */
  UUID getId();

  /**
   * @return владелец счета
   */
  IClient getClient();

  /**
   * @return банк, в котором зарегистрирован счет
   */
  IBank getBank();

  /**
   * Пополнение счета
   *
   * @param amount сумма пополнения
   */
  void payIn(MoneyAmount amount);

  /**
   * Снятие денег со счета
   *
   * @param amount сумма снятия
   */
  void withdraw(MoneyAmount amount);
}
