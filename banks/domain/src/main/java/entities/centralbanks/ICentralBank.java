package entities.centralbanks;

import classes.MoneyAmount;
import entities.bankaccounts.IBankAccount;
import entities.banks.IBank;
import entities.transactions.info.ICommandInfo;
import time.TimeService;

import java.util.UUID;

/**
 * Интерфейс для центрального банка
 * Декларирует функционал управления банками, взаимодействия с временным сервисом,
 * исполнения межбанковских переводов
 * Предполагается, что будет создан только один центральный банк для множества банков
 */
public interface ICentralBank {
  /**
   * @return временной сервис
   */
  TimeService getTimeService();

  /**
   * Зарегистрировать банк
   *
   * @param bank банк для отслеживания
   */
  void registerBank(IBank bank);

  /**
   * Получит банк по его идентификатору
   *
   * @param id id искомого банка
   * @return искомый банк (при обнаружении)
   * @throws RuntimeException при необнаружении банка
   */
  IBank getBank(UUID id);

  /**
   * Выполнить межбанковский перевод
   *
   * @param accountFrom счет отправителя
   * @param accountTo   счет получателя
   * @param amount      сумма перевода
   * @return объект-информирования о транзакции
   * @throws RuntimeException при попытке выполнить внутрибанковский перевод,
   * при попытке выполнить перевод сомнительному клиенту сверх лимита
   */
  ICommandInfo externalTransfer(
          IBankAccount accountFrom, IBankAccount accountTo, MoneyAmount amount);

  /**
   * Отменить межбанковскую транзакцию по ее номеру
   *
   * @param id id транзакции
   */
  void cancelTransaction(UUID id);
}
