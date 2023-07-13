import entities.clients.Client;
import exceptions.ClientBuilderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {
  @Test
  void CorrectCreation_NoThrow() {
    assertDoesNotThrow(
        () -> {
          Client.getBuilder().addFirstName("Suren").addSurname("Ermolaev").build();
        });
  }

  @Test
  void IncorrectCreation_ThrowException() {
    assertThrows(
        ClientBuilderException.class,
        () -> {
          Client.getBuilder().addFirstName("Suren").addPassport("228576587").build();
        });
  }

  @Test
  void DoubtfulAndNotDoubtful() {
    assertTrue(
        Client.getBuilder().addFirstName("Suren").addSurname("Ermolaev").build().isDoubtful());

    assertFalse(
        Client.getBuilder()
            .addFirstName("Suren")
            .addSurname("Ermolaev")
            .addPassport("228")
            .addAddress("ITMO")
            .build()
            .isDoubtful());
  }
}
