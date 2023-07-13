package chains;

import domain.BanksState;

public interface IHandler {
  boolean handleCommand(ConsoleCommand command, BanksState state);

  IHandler setNext(IHandler nextHandler);
}
