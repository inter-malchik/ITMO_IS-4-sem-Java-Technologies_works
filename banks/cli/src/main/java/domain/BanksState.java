package domain;

import entities.banks.Bank;
import entities.centralbanks.CentralBank;
import entities.clients.Client;
import time.TimeService;

import java.util.ArrayList;
import java.util.List;

public class BanksState {
  public TimeService timeService = new TimeService();
  public CentralBank centralBank = new CentralBank(timeService);
  public List<Client> clients = new ArrayList<>();
  public List<Bank> banks = new ArrayList<>();
}
