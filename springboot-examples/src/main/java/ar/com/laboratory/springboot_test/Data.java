package ar.com.laboratory.springboot_test;

import ar.com.laboratory.springboot_test.models.Account;
import ar.com.laboratory.springboot_test.models.Bank;

import java.math.BigDecimal;
import java.util.Optional;

public class Data {
    public static Optional<Account> createAccount001() {
        return Optional.of(new Account(1L, "Maximiliano", new BigDecimal("1000")));
    }

    public static Optional<Account> createAccount002() {
        return Optional.of(new Account(2L, "Jhon", new BigDecimal("2000")));
    }

    public static Optional<Bank> createBank() {
        return Optional.of(new Bank(1L, "El banco financiero", 0));
    }
}
