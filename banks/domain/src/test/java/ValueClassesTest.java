import classes.MoneyAmount;
import classes.PercentRate;
import exceptions.ValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValueClassesTest {
  @Test
  void MoneyAmount_CorrectCreation() {
    assertThrows(ValueException.class, () -> new MoneyAmount(-1.0));
    assertDoesNotThrow(() -> new MoneyAmount(0.0));
    assertDoesNotThrow(() -> new MoneyAmount(1000));
  }

  @Test
  void MoneyAmount_CorrectInvariant() {
    MoneyAmount moneyAmount = new MoneyAmount(4.53264283542587);
    assertTrue(4.529 < moneyAmount.getAmount() && moneyAmount.getAmount() < 4.531);
  }

  @Test
  void PercentRate_CorrectCreation() {
    assertThrows(ValueException.class, () -> new PercentRate(-0.5));
    assertThrows(ValueException.class, () -> new PercentRate(500));
    assertDoesNotThrow(() -> new PercentRate(0.5));
    assertDoesNotThrow(() -> new MoneyAmount(20));
  }

  @Test
  void PercentRate_CorrectInvariant() {
    PercentRate percentRate = new PercentRate(4.532);
    assertTrue(0.03 < percentRate.getRate() && percentRate.getRate() < 0.05);
  }
}
