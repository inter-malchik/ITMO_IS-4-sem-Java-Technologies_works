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
import entities.transactions.info.ICommandInfo;

import java.util.UUID;

/**
 * Интерфейс банка
 * Декларирует функционал регистрации клиентов,
 * открытия счетов,
 * исполнения внутрибанковских переводов,
 * исполнения простых операций над счетами,
 * хранение условий для банковских счетов и
 * оповещение подписанных клиентов об изменениях
 */
public interface IBank {
  /**
   * @return предел для операций снятия/перевода для сомнительных клиентов
   */
  MoneyAmount getDoubtfulClientOperationLimit();

  /**
   * @return соответствующий центральный банк
   */
  ICentralBank getCentralBank();

  /**
   * @return id банка
   */
  UUID getId();

  /**
   * Зарегистрировать клиента
   *
   * @param client регистрируемый клиент
   */
  void registerClient(Client client);

  /**
   * Получить клиента по его номеру паспорта
   *
   * @param id id клиента
   * @return искомый клиент (при обнаружении)
   * @throws RuntimeException в случае, если клиент не зарегистрирован
   */
  Client getClient(UUID id);

  /**
   * Открытие кредитного счета
   *
   * @param client предполагаемый владелец счета
   * @return объект кредитного счета
   */
  CreditAccount openCredit(Client client);

  /**
   * Открытие депозитарного счета
   *
   * @param client предполагаемый владелец счета
   * @return объект депозитарного счета
   */
  DepositAccount openDeposit(Client client);

  /**
   * Открытие дебетового счета
   *
   * @param client предполагаемый владелец счета
   * @return объект дебетового счета
   */
  DebitAccount openDebit(Client client);

  /**
   * Пополнение банковского счета
   *
   * @param account банковский счет
   * @param amount  сумма пополнения
   * @return объект информирования о транзакции
   */
  ICommandInfo payInMoney(IBankAccount account, MoneyAmount amount);

  /**
   * Снятие с банковского счета
   *
   * @param account банковский счет
   * @param amount  сумма снятия
   * @return объект информирования о транзакции
   * @throws RuntimeException при попытке превысить лимит сомнительному клиенту
   */
  ICommandInfo withdrawMoney(IBankAccount account, MoneyAmount amount);

  /**
   * Внутрибанковский перевод
   *
   * @param accountFrom счет отправителя
   * @param accountTo   счет получателя
   * @param amount      сумма перевода
   * @return объект информирования о транзакции
   * @throws RuntimeException при попытке превысить лимит сомнительному клиенту
   */
  ICommandInfo domesticTransfer(
          IBankAccount accountFrom, IBankAccount accountTo, MoneyAmount amount);

  /**
   * Отмена внутрибанковской транзакции по ее номеру
   *
   * @param id id транзакции
   */
  void cancelTransaction(UUID id);

  /**
   * Обновление условий кредитного счета
   * Все существующие счета будут подчиняться новым условиям
   * Клиенты, подписанные на обновления, будут оповещены
   *
   * @param newTerms новые условия
   */
  void updateCreditTerms(CreditTerms newTerms);

  /**
   * Обновление условий дебетового счета
   * Все существующие счета будут подчиняться новым условиям
   * Клиенты, подписанные на обновления, будут оповещены
   * @param newTerms новые условия
   */
  void updateDebitTerms(DebitTerms newTerms);

  /**
   * Обновление условий депозитарного счета
   * Все существующие счета будут подчиняться новым условиям
   * Клиенты, подписанные на обновления, будут оповещены
   * @param newTerms новые условия
   */
  void updateDepositTerms(DepositTerms newTerms);

  /**
   * Подписка клиента на изменения в условиях счетов
   *
   * @param client предполагаемый подписчик
   * @throws RuntimeException при попытке подписать не зарегистрированного клиента,
   * в случае если клиент уже подписан
   */
  void subscribe(Client client);

  /**
   * Отписка клиента на изменения в условиях счетов
   *
   * @param client предполагаемый отписчик
   * @throws RuntimeException при попытке отписать не зарегистрированного клиента,
   * в случае если клиент не был изначально подписан
   */
  void unSubscribe(Client client);
}
