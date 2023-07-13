package entities.transactions.info;

import entities.transactions.commands.ICommand;

import java.util.UUID;

/**
 * Интерфейс объекта информирования о транзакции
 * Инкапсулирует в себе работу с объектом команды, а также
 * добавляет дополнительный инвариант состояния
 */
public interface ICommandInfo {
  /**
   * @return инкапсулируемая команда
   */
  ICommand getCommand();

  /**
   * @return id объекта
   */
  UUID getId();

  /**
   * @return была ли выполнена команда
   */
  boolean isDone();

  /**
   * @return была ли отменена команда
   */
  boolean isCanceled();

  /**
   * Исполнить инкапсулируемую команду
   */
  void perform();

  /**
   * Отменить инкапсулируемую команду
   */
  void cancel();
}
