import entities.transactions.commands.ICommand;
import entities.transactions.service.CommandService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CommandServiceTest {
  static class EmptyCommand implements ICommand {
    @Override
    public void execute() {}

    @Override
    public void cancel() {}
  }

  @Test
  void CheckHistorySaving() {
    CommandService commandService = new CommandService();

    UUID firstId = commandService.performCommand(new EmptyCommand()).getId();
    UUID secondId = commandService.performCommand(new EmptyCommand()).getId();

    assertEquals(2, commandService.getCommandHistory().size());
    assertNotEquals(firstId, secondId);
  }

  @Test
  void CommandInfo_CheckInvariant() {
    CommandService commandService = new CommandService();
    assertTrue(commandService.performCommand(new EmptyCommand()).isDone());
    assertFalse(commandService.performCommand(new EmptyCommand()).isCanceled());
  }
}
