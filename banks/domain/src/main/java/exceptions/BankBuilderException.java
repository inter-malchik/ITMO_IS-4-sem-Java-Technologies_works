package exceptions;

public class BankBuilderException extends BanksBaseException {
  private BankBuilderException(String message) {
    super(message);
  }

  public static BankBuilderException NoDoubtfulClientLimitSpecified() {
    return new BankBuilderException("NoDoubtfulClientLimitSpecified");
  }

  public static BankBuilderException NoCreditTermsSpecified() {
    return new BankBuilderException("NoCreditTermsSpecified");
  }

  public static BankBuilderException NoDepositTermsSpecified() {
    return new BankBuilderException("NoDepositTermsSpecified");
  }

  public static BankBuilderException NoDebitTermsSpecified() {
    return new BankBuilderException("NoDebitTermsSpecified");
  }

  public static BankBuilderException NoCentralBankSpecified() {
    return new BankBuilderException("NoCentralBankSpecified");
  }
}
