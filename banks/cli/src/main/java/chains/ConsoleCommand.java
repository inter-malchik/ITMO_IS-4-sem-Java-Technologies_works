package chains;

import java.util.Scanner;

public class ConsoleCommand {
  private final String[] commands;

  public ConsoleCommand(String commandString) {
    commands = commandString.split("[^A-Za-z-_0-9]+");
  }

  public String[] getCommands() {
    return commands;
  }

  public static ConsoleCommand parse() {
    return new ConsoleCommand(new Scanner(System.in).nextLine());
  }
}
