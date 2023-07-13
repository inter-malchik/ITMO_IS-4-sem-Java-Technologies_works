package entities.transactions.info;

import entities.transactions.commands.ICommand;
import exceptions.TransactionsException;

import java.util.UUID;

/**
 * Имплементация объекта информирования о транзакции
 */
public class CommandInfo implements ICommandInfo {
  private boolean done;
  private boolean canceled;
  private final ICommand command;
  private final UUID id;

  /**
   * Создание объекта над объектом команды
   * (по аналогии декоратора)
   * @param command команда над которой берется управление
   */
  public CommandInfo(ICommand command) {
    this.command = command;
    this.done = this.canceled = false;
    this.id = UUID.randomUUID();
  }

  @Override
  public ICommand getCommand() {
    return command;
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public boolean isDone() {
    return done;
  }

  @Override
  public boolean isCanceled() {
    return canceled;
  }

  @Override
  public void perform() {
    if (done) {
      throw TransactionsException.AlreadyPerformed(this);
    }
    command.execute();
    done = true;
  }

  @Override
  public void cancel() {
    if (!done) {
      throw TransactionsException.HasNotBeenPerformed(this);
    }
    if (canceled) {
      throw TransactionsException.AlreadyCanceled(this);
    }
    command.cancel();
    canceled = true;
  }
}
