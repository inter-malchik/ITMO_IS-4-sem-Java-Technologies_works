package chains.concretehandlers;

import chains.ConsoleCommand;
import chains.IHandler;
import domain.BanksState;

import java.util.Arrays;

public class NoFoundHandler implements IHandler {
  @Override
  public boolean handleCommand(ConsoleCommand command, BanksState state) {
    System.out.println("Couldn't Parse: " + Arrays.toString(command.getCommands()));
    return true;
  }

  @Override
  public IHandler setNext(IHandler nextHandler) {
    throw new RuntimeException("unreachable");
  }
}
