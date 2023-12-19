package ar.com.laboratory.springboot_test.models;

import ar.com.laboratory.springboot_test.exceptions.InsufficientFundsException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name="accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String person;
    private BigDecimal balance;

    public Account() {
    }

    public Account(Long id, String person, BigDecimal balance) {
        this.id = id;
        this.person = person;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void debit(BigDecimal monto) {
        BigDecimal nuevoSaldo = this.balance.subtract(monto);
        if(nuevoSaldo.compareTo(BigDecimal.ZERO) < 0){
            throw new InsufficientFundsException("Insufficient money in the account.");
        }
        this.balance = nuevoSaldo;
    }

    public void credit(BigDecimal monto) {
        this.balance = balance.add(monto);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account cuenta = (Account) o;
        return Objects.equals(id, cuenta.id) && Objects.equals(person, cuenta.person) && Objects.equals(balance, cuenta.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, balance);
    }
}
