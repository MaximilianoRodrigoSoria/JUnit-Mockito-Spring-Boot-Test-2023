package org.laboratory.junit5.examples.models;

import org.laboratory.junit5.examples.utils.exceptions.InsufficientBalanceException;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {

    private String person;
    private BigDecimal balance;

    private Bank bank;


    public Account(String person, BigDecimal balance) {
        this.person = person;
        this.balance = balance;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public void debit(BigDecimal ammount){
        var newBalance = this.balance.subtract(ammount);
        if(newBalance.compareTo(BigDecimal.ZERO)< 0){
            throw new InsufficientBalanceException("Insufficient balance");
        } else {
            this.balance = newBalance;
        }


    }

    public  void credit(BigDecimal ammount){
        this.balance = this.balance.add(ammount);

    }
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Account)){
            return false;
        }
        Account a = (Account) obj;
        if(Objects.isNull(this.balance) || Objects.isNull(this.person)){
            return false;
        }
        return a.getBalance().equals(this.balance) && a.getPerson().equals(this.person);
    }


}
