import classes.MoneyAmount;
import entities.banks.Bank;
import entities.centralbanks.CentralBank;
import exceptions.BankBuilderException;
import exceptions.CentralBankException;
import org.junit.jupiter.api.Test;
import time.TimeService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BanksTest {
  @Test
  void CentralBank_SeekUnexistantBank() {
    assertThrows(
        CentralBankException.class,
        () -> new CentralBank(new TimeService()).getBank(UUID.randomUUID()));
  }

  @Test
  void BankBuilder_CheckCreationCorrectness() {
    assertThrows(
        BankBuilderException.class,
        () -> Bank.BankBuilder().addDoubtfulClientLimit(new MoneyAmount(100)).build());
  }
}
