package entities.transactions.service;

import entities.transactions.commands.ICommand;
import entities.transactions.history.CommandHistory;
import entities.transactions.info.CommandInfo;
import entities.transactions.info.ICommandInfo;
import exceptions.TransactionsException;

import java.util.Optional;
import java.util.UUID;

/**
 * Имплементация сервиса для работы с транзакциями
 */
public class CommandService implements ICommandService {
  private final CommandHistory commandHistory;

  /**
   * Инстанцирование по умолчанию. Инициализируется история транзакций
   */
  public CommandService() {
    commandHistory = new CommandHistory();
  }

  /**
   * Получить историю транзакций
   *
   * @return объект истории транзакций
   */
  public CommandHistory getCommandHistory() {
    return commandHistory;
  }

  @Override
  public ICommandInfo performCommand(ICommand command) {
    ICommandInfo info = new CommandInfo(command);

    info.perform();
    commandHistory.push(info);
    return info;
  }

  @Override
  public void cancelCommand(UUID id) {
    Optional<ICommandInfo> info = commandHistory.find(id);
    if (!info.isPresent()) {
      throw TransactionsException.CommandNotFound(id);
    }
    info.get().cancel();
  }
}
