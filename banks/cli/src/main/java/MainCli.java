import chains.ConsoleCommand;
import chains.IHandler;
import chains.concretehandlers.ExitHandler;
import chains.concretehandlers.HelpHandler;
import chains.concretehandlers.NoFoundHandler;
import chains.concretehandlers.PersonsHandler;
import domain.BanksState;

public class MainCli {
  public static void main(String[] args) {
    BanksState state = new BanksState();

    IHandler baseHandler = new HelpHandler();
    baseHandler
        .setNext(new PersonsHandler())
        .setNext(new ExitHandler())
        .setNext(new NoFoundHandler());

    System.out.println("CENTRAL BANK: " + state.centralBank.getId());
    while (true) {
      baseHandler.handleCommand(ConsoleCommand.parse(), state);
    }
  }
}
