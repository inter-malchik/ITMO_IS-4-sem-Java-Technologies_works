package entities.centralbanks;

import classes.MoneyAmount;
import entities.bankaccounts.IBankAccount;
import entities.banks.IBank;
import entities.transactions.commands.TransferCommand;
import entities.transactions.info.ICommandInfo;
import entities.transactions.service.CommandService;
import exceptions.BankException;
import exceptions.CentralBankException;
import time.TimeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Имплементация центрального банка
 */
public class CentralBank implements ICentralBank {
  private final List<IBank> banks;
  private final CommandService externalService;
  private final TimeService timeService;
  private final UUID id;

  /**
   * Инстанцировать центральный банк
   *
   * @param timeService временной сервис
   */
  public CentralBank(TimeService timeService) {
    this.timeService = timeService;
    externalService = new CommandService();
    id = UUID.randomUUID();
    banks = new ArrayList<>();
  }

  /**
   * @return id объекта
   */
  public UUID getId() {
    return id;
  }

  @Override
  public TimeService getTimeService() {
    return timeService;
  }

  /**
   * Проверка на то, отслеживается ли банк
   *
   * @param bank объект банка
   * @return истина/ложь
   */
  public boolean isBankRegistered(IBank bank) {
    return banks.contains(bank);
  }

  @Override
  public void registerBank(IBank bank) {
    if (isBankRegistered(bank)) {
      throw CentralBankException.BankAlreadyRegistered(bank);
    }
    banks.add(bank);
  }

  @Override
  public IBank getBank(UUID id) {
    Optional<IBank> bankOptional = banks.stream().filter(bank -> bank.getId() == id).findFirst();
    if (!bankOptional.isPresent()) {
      throw CentralBankException.BankNotRegistered(id);
    }
    return bankOptional.get();
  }

  @Override
  public ICommandInfo externalTransfer(
          IBankAccount accountFrom, IBankAccount accountTo, MoneyAmount amount) {
    checkBankRegistration(accountFrom.getBank());
    checkBankRegistration(accountTo.getBank());
    if (accountFrom.getBank() == accountTo.getBank()) {
      throw CentralBankException.TryingToDoDomesticTransfer(accountFrom, accountTo);
    }
    if (accountFrom.getClient().isDoubtful()
            && accountFrom.getBank().getDoubtfulClientOperationLimit().isMoreThan(amount)) {
      throw BankException.DoubtfulLimitExceeded(
              accountFrom.getClient(), accountFrom.getBank().getDoubtfulClientOperationLimit(), amount);
    }

    return externalService.performCommand(new TransferCommand(accountFrom, accountTo, amount));
  }

  private void checkBankRegistration(IBank bank) {
    if (!isBankRegistered(bank)) {
      throw CentralBankException.BankNotRegistered(bank);
    }
  }

  @Override
  public void cancelTransaction(UUID id) {
    externalService.cancelCommand(id);
  }
}
