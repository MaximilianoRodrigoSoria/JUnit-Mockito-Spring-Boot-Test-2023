package ar.com.laboratory.springboot_test;


import ar.com.laboratory.springboot_test.models.Account;
import ar.com.laboratory.springboot_test.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@Sql({"/import.sql"}) - Se importa manualmente el scrip
public class JPAIntegrationsTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    void testFindById() {

        //Assemble
        accountRepository.findAll();

        //Act
        Optional<Account> account = accountRepository.findById(1L);

        //Assert
        assertTrue(account.isPresent());
        assertEquals("Maximiliano",account.orElseThrow().getPerson());

    }

    @Test
    void testFindByPerson() {

        //Assemble
        accountRepository.findAll();

        //Act
        Optional<Account> account = accountRepository.findByPerson("Maximiliano");

        //Assert
        assertTrue(account.isPresent());
        assertEquals("Maximiliano",account.orElseThrow().getPerson());
        assertEquals("1000.00",account.orElseThrow().getBalance().toPlainString());

    }

    @Test
    void testFindByPersonAndNotExist() {
        //Assemble //Act
        Optional<Account> account = accountRepository.findByPerson("Pepe");

        //Assert
        var exception = assertThrows(NoSuchElementException.class, ()-> account.orElseThrow());
        assertEquals("No value present", exception.getMessage());
        assertFalse(account.isPresent());
    }


    @Test
    void testFindAll() {
        //Assemble //Act
        List<Account> accounts = accountRepository.findAll();

        //Assert
        assertFalse(accounts.isEmpty());
        assertEquals(2,accounts.size());
    }

    @Test
    void testSave() {
        //Assemble
        Account accountToPersist = new Account(null, "Pepe", new BigDecimal(3000));
        // Act
        Account accountPersist = accountRepository.save(accountToPersist);
        Account account = accountRepository.findById(accountPersist.getId()).orElseThrow();

        //Assert
        assertEquals("Pepe",account.getPerson());
        assertEquals("3000", account.getBalance().toPlainString());
    }

    @Test
    void testUpdate() {
        //Assemble
        Account accountToPersist = new Account(null, "Pepe", new BigDecimal(3000));
        // Act
        Account accountPersist = accountRepository.save(accountToPersist);
        Account account = accountRepository.findById(accountPersist.getId()).orElseThrow();

        //Assert
        assertEquals("Pepe",account.getPerson());
        assertEquals("3000", account.getBalance().toPlainString());


        account.setBalance(new BigDecimal(5000));
        Account accountUpdate = accountRepository.save(account);

        //Assert
        assertEquals("Pepe",accountUpdate.getPerson());
        assertEquals("5000", accountUpdate.getBalance().toPlainString());
    }

    @Test
    void testDelete() {

        Account account = accountRepository.findById(2L).orElseThrow();

        assertEquals("John",account.getPerson());

        accountRepository.delete(account);

        Optional<Account> deleteAccount = accountRepository.findByPerson("John");

        var exception = assertThrows(NoSuchElementException.class, deleteAccount::get);

        assertEquals("No value present", exception.getMessage());

        assertFalse(deleteAccount.isPresent());
    }
}
