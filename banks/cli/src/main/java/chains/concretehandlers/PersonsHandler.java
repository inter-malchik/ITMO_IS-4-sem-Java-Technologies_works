package chains.concretehandlers;

import chains.ConsoleCommand;
import chains.IHandler;
import domain.BanksState;
import entities.clients.Client;

import static java.util.Arrays.copyOfRange;

public class PersonsHandler implements IHandler {
  private IHandler next = null;

  @Override
  public boolean handleCommand(ConsoleCommand command, BanksState state) {
    if (command.getCommands()[0].equals("create_person")) {
      createPerson(copyOfRange(command.getCommands(), 1, command.getCommands().length), state);
      return true;
    }
    if (command.getCommands()[0].equals("persons")) {
      showPersons(state);
      return true;
    }
    return (next != null) ? next.handleCommand(command, state) : false;
  }

  private void showPersons(BanksState state) {
    if (state.clients.isEmpty()) {
      System.out.println("there are no persons...");
      return;
    }
    for (Client client : state.clients) {
      System.out.println(client.toString());
    }
  }

  private void createPerson(String[] tokens, BanksState state) {
    System.out.println(tokens);
    if (tokens.length < 2 || tokens.length > 4) {
      System.out.println("Incorrect command form");
      return;
    }
    Client newClient = null;
    switch (tokens.length) {
      case 2:
        newClient = Client.getBuilder().addFirstName(tokens[0]).addSurname(tokens[1]).build();
        break;
      case 3:
        newClient =
            Client.getBuilder()
                .addFirstName(tokens[0])
                .addSurname(tokens[1])
                .addPassport(tokens[2])
                .build();
        break;
      case 4:
        newClient =
            Client.getBuilder()
                .addFirstName(tokens[0])
                .addSurname(tokens[1])
                .addPassport(tokens[2])
                .addAddress(tokens[3])
                .build();
        break;
    }

    state.clients.add(newClient);
    System.out.println("Created " + newClient.toString());
  }

  @Override
  public IHandler setNext(IHandler nextHandler) {
    next = nextHandler;
    return next;
  }
}
