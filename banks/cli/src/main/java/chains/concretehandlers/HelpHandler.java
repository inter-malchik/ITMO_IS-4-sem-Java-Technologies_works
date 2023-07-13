package chains.concretehandlers;

import chains.ConsoleCommand;
import chains.IHandler;
import domain.BanksState;

import java.util.Objects;

public class HelpHandler implements IHandler {
  private IHandler next = null;

  @Override
  public boolean handleCommand(ConsoleCommand command, BanksState state) {
    if (Objects.equals(command.getCommands()[0], "help")) {
      showHelp();
      return true;
    }
    return (next != null) ? next.handleCommand(command, state) : false;
  }

  public static void showHelp() {
    System.out.println("create_person {NAME} {SURNAME} [ID] [ADDRESS] -> создать пользователя");
    System.out.println("persons -> вывести всех созданных пользоваталей");
    System.out.println("exit -> завершить работу");
  }

  @Override
  public IHandler setNext(IHandler nextHandler) {
    next = nextHandler;
    return next;
  }
}
