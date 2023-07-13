import classes.MoneyAmount;
import classes.PercentRate;
import entities.bankaccounts.deposit.DepositTerms;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountsTest {
  @Test
  void DepositTermsCreation_NoThrow() {
    assertDoesNotThrow(
        () -> {
          DepositTerms.getBuilder()
              .addInterestBound(new MoneyAmount(0.0), new PercentRate(3))
              .addInterestBound(new MoneyAmount(50000), new PercentRate(3.5))
              .addInterestBound(new MoneyAmount(100000), new PercentRate(4.0))
              .setTimeSpan(DepositTerms.DepositTimeSpans.sixMonth())
              .build();
        });
  }

  @Test
  void DepositTerms_CorrectInvariant() {
    DepositTerms terms =
        DepositTerms.getBuilder()
            .addInterestBound(new MoneyAmount(0.0), new PercentRate(3))
            .addInterestBound(new MoneyAmount(100000), new PercentRate(4.0))
            .addInterestBound(new MoneyAmount(50000), new PercentRate(3.5))
            .setTimeSpan(DepositTerms.DepositTimeSpans.oneMonth())
            .build();

    assertEquals(3, terms.getBalanceInterests().size());

    Object[] sortedBounds = terms.getBalanceInterests().keySet().stream().sorted().toArray();
    Object[] unsortedBounds = terms.getBalanceInterests().keySet().stream().toArray();

    assertEquals(sortedBounds.length, unsortedBounds.length);

    for (int i = 0; i < sortedBounds.length; i++) {
      assertTrue(sortedBounds[i] instanceof MoneyAmount);
      assertTrue(unsortedBounds[i] instanceof MoneyAmount);
      assertTrue(((MoneyAmount) sortedBounds[i]).isEqualWith((MoneyAmount) unsortedBounds[i]));
    }
  }
}
