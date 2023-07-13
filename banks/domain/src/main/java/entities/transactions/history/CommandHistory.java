package entities.transactions.history;

import entities.transactions.info.ICommandInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * История команд
 * Содержит в себе информацию об исполненных транзакциях
 * @see ICommandInfo
 */
public class CommandHistory {
  private final List<ICommandInfo> history;

  /**
   * Инициализировать пустую очередь
   */
  public CommandHistory() {
    history = new ArrayList<>();
  }

  /**
   * Добавить в историю команду
   *
   * @param info фиксируемая команда
   */
  public void push(ICommandInfo info) {
    history.add(info);
  }

  /**
   * Find optional.
   *
   * @param id id искомой команды
   * @return результат поиска
   */
  public Optional<ICommandInfo> find(UUID id) {
    return history.stream().filter(info -> info.getId() == id).findFirst();
  }

  /**
   * @return количество зафиксированных команд
   */
  public int size() {
    return history.size();
  }
}
