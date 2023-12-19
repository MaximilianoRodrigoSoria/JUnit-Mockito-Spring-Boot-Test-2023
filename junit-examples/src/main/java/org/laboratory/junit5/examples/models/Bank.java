package org.laboratory.junit5.examples.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Bank {

    private String name;

    private List<Account> accounts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public Bank() {
        this.accounts = new ArrayList<>();
    }

    public void transfer(Account origin, Account destiny, BigDecimal ammount)
    {
        origin.debit(ammount);
        destiny.credit(ammount);
    }

    public void addAccount(Account account){
        this.accounts.add(account);
        account.setBank(this);
    }
}
