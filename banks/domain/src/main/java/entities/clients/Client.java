package entities.clients;

import exceptions.ClientBuilderException;

import java.util.UUID;

/**
 * Клиент банка (имплементация по умолчанию)
 * Создается только через строителя в связи с необходимостью валидации полей
 */
public class Client implements IClient {

  private final String name;

  private final String surname;

  private final String address;

  private final String passportNumber;

  private final UUID id;

  private Client(String name, String surname, String address, String passportNumber) {
    this.name = name;
    this.surname = surname;
    this.address = address;
    this.passportNumber = passportNumber;
    this.id = UUID.randomUUID();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getSurname() {
    return surname;
  }

  @Override
  public String getAddress() {
    return address;
  }

  @Override
  public String getPassportNumber() {
    return passportNumber;
  }

  @Override
  public boolean isDoubtful() {
    return passportNumber == null
            || passportNumber.isEmpty()
            || address == null
            || address.isEmpty();
  }

  /**
   * @return получить строителя класса
   */
  public static ClientBuilder getBuilder() {
    return new ClientBuilder();
  }

  @Override
  public UUID getId() {
    return id;
  }

  /**
   * Строитель для клиента банка. Валидирует поля name и surname
   */
  public static class ClientBuilder {
    private String name = null;
    private String surname = null;
    private String address = null;
    private String passport = null;

    /**
     * Добавить имя
     * (Обязательно для создания объекта)
     *
     * @param name имя
     * @return объект строителя
     */
    public ClientBuilder addFirstName(String name) {
      this.name = name;
      return this;
    }

    /**
     * Добавить фамилию
     * (Обязательно для создания объекта)
     *
     * @param surname фамилия
     * @return объект строителя
     */
    public ClientBuilder addSurname(String surname) {
      this.surname = surname;
      return this;
    }

    /**
     * Добавить адрес
     * (опционально)
     *
     * @param address адрес
     * @return объект строителя
     */
    public ClientBuilder addAddress(String address) {
      this.address = address;
      return this;
    }

    /**
     * Добавить номер паспорта
     * (опционально)
     *
     * @param passport номер паспорта
     * @return объект строителя
     */
    public ClientBuilder addPassport(String passport) {
      this.passport = passport;
      return this;
    }

    /**
     * @return Создать объект клиента
     * @throws ClientBuilderException проверяет поля на указание имени и фамилии
     */
    public Client build() {
      if (name == null || name.isEmpty()) {
        throw ClientBuilderException.NoNameSpecified();
      }

      if (surname == null || surname.isEmpty()) {
        throw ClientBuilderException.NoSurnameSpecified();
      }

      return new Client(name, surname, address, passport);
    }
  }

  @Override
  public String toString() {
    return "Client{" + "name='" + name + '\'' + ", surname='" + surname + '\'' + ", id=" + id + '}';
  }
}
