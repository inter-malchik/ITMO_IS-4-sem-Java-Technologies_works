package exceptions;

import classes.MoneyAmount;
import entities.bankaccounts.IBankAccount;
import entities.clients.IClient;

import java.util.UUID;

public class BankException extends BanksBaseException {
  private BankException(String message) {
    super(message);
  }

  public static BankException ClientNotRegistered(IClient client) {
    return new BankException(String.format("client %b is not registered", client));
  }

  public static BankException ClientNotRegistered(UUID id) {
    return new BankException(String.format("client %b is not registered", id));
  }

  public static BankException ClientAlreadyRegistered(IClient client) {
    return new BankException(String.format("client %b has already registered", client));
  }

  public static BankException ClientAlreadySubscribed(IClient client) {
    return new BankException(String.format("client %b is already subscribed", client));
  }

  public static BankException ClientAlreadyUnsubscribed(IClient client) {
    return new BankException(String.format("client %b has already unsubscribed", client));
  }

  public static BankException AccountNotRegistered(IBankAccount account) {
    return new BankException(String.format("account (%b) is not registered", account.getId()));
  }

  public static BankException DoubtfulLimitExceeded(
      IClient client, MoneyAmount limit, MoneyAmount amount) {
    return new BankException(
        String.format("client %b can't withdraw %b (limit: %b)", client, amount, limit));
  }
}
