package entities.transactions.service;

import entities.transactions.commands.ICommand;
import entities.transactions.info.ICommandInfo;

import java.util.UUID;

/**
 * Интерфейс для сервиса работы с транзакциями
 * инкапсулирует работу с командами и выдачу объектов информирования
 */
public interface ICommandService {
  /**
   * Исполнить произвольную транзакцию
   *
   * @param command транзакция
   * @return объект информирования о транзакции
   */
  ICommandInfo performCommand(ICommand command);

  /**
   * Отменить выполненную транзакцию
   *
   * @param id id транзакции
   */
  void cancelCommand(UUID id);
}
