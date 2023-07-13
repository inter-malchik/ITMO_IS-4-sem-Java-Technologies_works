package entities.banks;

import classes.MoneyAmount;
import entities.bankaccounts.IBankAccount;
import entities.bankaccounts.credit.CreditAccount;
import entities.bankaccounts.credit.CreditTerms;
import entities.bankaccounts.debit.DebitAccount;
import entities.bankaccounts.debit.DebitTerms;
import entities.bankaccounts.deposit.DepositAccount;
import entities.bankaccounts.deposit.DepositTerms;
import entities.centralbanks.ICentralBank;
import entities.clients.Client;
import entities.clients.IClient;
import entities.transactions.commands.PayInCommand;
import entities.transactions.commands.TransferCommand;
import entities.transactions.commands.WithdrawCommand;
import entities.transactions.info.ICommandInfo;
import entities.transactions.service.CommandService;
import exceptions.BankBuilderException;
import exceptions.BankException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Имплементация банка
 * @see DebitAccount
 * @see DepositTerms
 * @see CreditAccount
 * @see ICentralBank
 */
public class Bank implements IBank {
  private final List<Client> clients;
  private final List<IBankAccount> accounts;
  private final CommandService domesticService;

  private final CreditTerms creditTerms;
  private final DebitTerms debitTerms;
  private final DepositTerms depositTerms;

  private final ArrayList<Client> subscribedClients;

  private final MoneyAmount doubtfulClientOperationLimit;

  private final ICentralBank centralBank;
  private final UUID id;

  private Bank(
          MoneyAmount doubtfulLimit,
          CreditTerms creditTerms,
          DebitTerms debitTerms,
          DepositTerms depositTerms,
          ICentralBank centralBank) {
    this.doubtfulClientOperationLimit = doubtfulLimit;
    this.id = UUID.randomUUID();
    this.clients = new ArrayList<>();
    this.accounts = new ArrayList<>();
    this.domesticService = new CommandService();
    this.subscribedClients = new ArrayList<>();
    this.creditTerms = creditTerms;
    this.debitTerms = debitTerms;
    this.depositTerms = depositTerms;
    this.centralBank = centralBank;
    centralBank.registerBank(this);
  }

  @Override
  public ICentralBank getCentralBank() {
    return centralBank;
  }

  @Override
  public MoneyAmount getDoubtfulClientOperationLimit() {
    return doubtfulClientOperationLimit;
  }

  @Override
  public UUID getId() {
    return id;
  }

  /**
   * Получить все банковские счета клиента
   *
   * @param client клиент банка
   * @return список счетов, принадлежащих клиенту
   */
  public List<IBankAccount> personAccounts(IClient client) {
    return Collections.unmodifiableList(
            accounts.stream()
                    .filter(account -> account.getClient() == client)
                    .collect(Collectors.toList()));
  }

  /**
   * Проверка на то, зарегистрирован ли клиент в банке
   *
   * @param client предполагаемый клиент банка
   * @return истина/ложь
   */
  public boolean isClientRegistered(Client client) {
    return clients.contains(client);
  }

  /**
   * Проверка на то, зарегистрирован ли счет в банке
   *
   * @param account предполагаемый банковский счет
   * @return истина/ложь
   */
  public boolean isAccountRegistered(IBankAccount account) {
    return accounts.contains(account);
  }

  @Override
  public void registerClient(Client client) {
    if (isClientRegistered(client)) {
      throw BankException.ClientAlreadyRegistered(client);
    }
    clients.add(client);
  }

  @Override
  public Client getClient(UUID id) {
    Optional<Client> optionalClient =
            clients.stream().filter(client -> client.getId() == id).findFirst();
    if (!optionalClient.isPresent()) {
      throw BankException.ClientNotRegistered(id);
    }
    return optionalClient.get();
  }

  private void checkClientRegistration(Client client) {
    if (!isClientRegistered(client)) {
      throw BankException.ClientNotRegistered(client);
    }
  }

  private void checkAccountRegistration(IBankAccount account) {
    if (!isAccountRegistered(account)) {
      throw BankException.AccountNotRegistered(account);
    }
  }

  @Override
  public CreditAccount openCredit(Client client) {
    checkClientRegistration(client);
    CreditAccount newAccount = new CreditAccount(creditTerms, client, this);
    accounts.add(newAccount);
    return newAccount;
  }

  @Override
  public DepositAccount openDeposit(Client client) {
    checkClientRegistration(client);
    DepositAccount newAccount = new DepositAccount(depositTerms, client, this);
    accounts.add(newAccount);
    return newAccount;
  }

  @Override
  public DebitAccount openDebit(Client client) {
    checkClientRegistration(client);
    DebitAccount newAccount = new DebitAccount(debitTerms, client, this);
    accounts.add(newAccount);
    return newAccount;
  }

  @Override
  public ICommandInfo payInMoney(IBankAccount account, MoneyAmount amount) {
    checkAccountRegistration(account);
    return domesticService.performCommand(new PayInCommand(account, amount));
  }

  @Override
  public ICommandInfo withdrawMoney(IBankAccount account, MoneyAmount amount) {
    checkAccountRegistration(account);
    if (account.getClient().isDoubtful() && getDoubtfulClientOperationLimit().isMoreThan(amount)) {
      throw BankException.DoubtfulLimitExceeded(
              account.getClient(), getDoubtfulClientOperationLimit(), amount);
    }
    return domesticService.performCommand(new WithdrawCommand(account, amount));
  }

  @Override
  public ICommandInfo domesticTransfer(
          IBankAccount accountFrom, IBankAccount accountTo, MoneyAmount amount) {
    checkAccountRegistration(accountFrom);
    checkAccountRegistration(accountTo);
    if (accountFrom.getClient().isDoubtful()
            && getDoubtfulClientOperationLimit().isMoreThan(amount)) {
      throw BankException.DoubtfulLimitExceeded(
              accountFrom.getClient(), getDoubtfulClientOperationLimit(), amount);
    }
    return domesticService.performCommand(new TransferCommand(accountFrom, accountTo, amount));
  }

  @Override
  public void cancelTransaction(UUID id) {
    domesticService.cancelCommand(id);
  }

  @Override
  public void updateCreditTerms(CreditTerms newTerms) {
    creditTerms.update(newTerms);
    subscribedClients.stream()
            .filter(
                    pers ->
                            personAccounts(pers).stream().anyMatch(account -> account instanceof CreditAccount))
            .forEach(Object::notify);
  }

  @Override
  public void updateDebitTerms(DebitTerms newTerms) {
    debitTerms.update(newTerms);
    subscribedClients.stream()
            .filter(
                    pers ->
                            personAccounts(pers).stream().anyMatch(account -> account instanceof DebitAccount))
            .forEach(Object::notify);
  }

  @Override
  public void updateDepositTerms(DepositTerms newTerms) {
    depositTerms.update(newTerms);
    subscribedClients.stream()
            .filter(
                    pers ->
                            personAccounts(pers).stream()
                                    .anyMatch(account -> account instanceof DepositAccount))
            .forEach(Object::notify);
  }

  @Override
  public void subscribe(Client client) {
    checkClientRegistration(client);
    if (subscribedClients.contains(client)) {
      throw BankException.ClientAlreadySubscribed(client);
    }
    subscribedClients.add(client);
  }

  @Override
  public void unSubscribe(Client client) {
    checkClientRegistration(client);
    if (!subscribedClients.contains(client)) {
      throw BankException.ClientAlreadyUnsubscribed(client);
    }
    subscribedClients.remove(client);
  }

  /**
   * @return класс-строитель для банка
   */
  public static BankBuilder BankBuilder() {
    return new BankBuilder();
  }

  /**
   * Класс-строитель для банка
   */
  public static class BankBuilder {
    private MoneyAmount doubtfulClientLimit = null;
    private CreditTerms creditTerms = null;
    private DebitTerms debitTerms = null;
    private DepositTerms depositTerms = null;
    private ICentralBank centralBank = null;

    /**
     * Добавить лимит снятий/переводой для сомнительных клиентов
     * (Обязательно для создания объекта)
     *
     * @param limit лимит снятий/переводов
     * @return объект строителя
     */
    public BankBuilder addDoubtfulClientLimit(MoneyAmount limit) {
      doubtfulClientLimit = limit;
      return this;
    }

    /**
     * Добавить условия для кредитных счетов
     * (Обязательно для создания объекта)
     *
     * @param terms условия
     * @return объект строителя
     */
    public BankBuilder addCreditTerms(CreditTerms terms) {
      creditTerms = terms;
      return this;
    }

    /**
     * Добавить условия для дебетовых счетов
     * (Обязательно для создания объекта)
     *
     * @param terms условия
     * @return объект строителя
     */
    public BankBuilder addDebitTerms(DebitTerms terms) {
      debitTerms = terms;
      return this;
    }

    /**
     * Добавить условия для депозитарных счетов
     * (Обязательно для создания объекта)
     *
     * @param terms условия
     * @return объект строителя
     */
    public BankBuilder addDepositTerms(DepositTerms terms) {
      depositTerms = terms;
      return this;
    }

    /**
     * Добавить центральный банк
     * (Обязательно для создания объекта)
     *
     * @param cBank the c bank
     * @return объект строителя
     */
    public BankBuilder addCentralBank(ICentralBank cBank) {
      centralBank = cBank;
      return this;
    }

    /**
     * @return созданный объект банка
     * @throws BankBuilderException в случае, если не установлено одно из полей
     */
    public Bank build() {
      if (doubtfulClientLimit == null) {
        throw BankBuilderException.NoDoubtfulClientLimitSpecified();
      }

      if (creditTerms == null) {
        throw BankBuilderException.NoCreditTermsSpecified();
      }

      if (debitTerms == null) {
        throw BankBuilderException.NoDebitTermsSpecified();
      }

      if (depositTerms == null) {
        throw BankBuilderException.NoDepositTermsSpecified();
      }

      if (centralBank == null) {
        throw BankBuilderException.NoCentralBankSpecified();
      }

      return new Bank(doubtfulClientLimit, creditTerms, debitTerms, depositTerms, centralBank);
    }
  }
}
