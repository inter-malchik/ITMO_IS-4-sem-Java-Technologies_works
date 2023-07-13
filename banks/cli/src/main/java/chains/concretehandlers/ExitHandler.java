package chains.concretehandlers;

import chains.ConsoleCommand;
import chains.IHandler;
import domain.BanksState;

import java.util.Objects;

public class ExitHandler implements IHandler {
  private IHandler next = null;

  @Override
  public boolean handleCommand(ConsoleCommand command, BanksState state) {
    if (Objects.equals(command.getCommands()[0], "exit")) {
      System.exit(0);
      return true;
    }
    return (next != null) ? next.handleCommand(command, state) : false;
  }

  @Override
  public IHandler setNext(IHandler nextHandler) {
    next = nextHandler;
    return next;
  }
}
