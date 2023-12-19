package ar.com.laboratory.springboot_test;

import ar.com.laboratory.springboot_test.exceptions.InsufficientFundsException;
import ar.com.laboratory.springboot_test.models.Account;
import ar.com.laboratory.springboot_test.models.Bank;
import ar.com.laboratory.springboot_test.repositories.AccountRepository;
import ar.com.laboratory.springboot_test.repositories.BankRepository;
import ar.com.laboratory.springboot_test.services.AccountService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpringbootTestApplicationTests {



    @MockBean
    AccountRepository accountRepository;

    @MockBean
    BankRepository bankRepository;


    @Autowired
    AccountService accountService;
    @Test
    void testTransferAmount() {
        when(accountRepository.findById(1L)).thenReturn(Data.createAccount001());
        when(accountRepository.findById(2L)).thenReturn(Data.createAccount002());
        when(bankRepository.findById(1L)).thenReturn(Data.createBank());


        BigDecimal originAccountBalance = accountService.checkBalance(1L);
        BigDecimal destinyAccountBalance = accountService.checkBalance(2L);

        assertEquals("1000", originAccountBalance.toPlainString());
        assertEquals("2000", destinyAccountBalance.toPlainString());

        accountService.transfer(1L,2L,new BigDecimal("100"), 1L);

        originAccountBalance = accountService.checkBalance(1L);
        destinyAccountBalance = accountService.checkBalance(2L);

        assertEquals("900", originAccountBalance.toPlainString());
        assertEquals("2100", destinyAccountBalance.toPlainString());

        int total = accountService.checkTotalTransfer(1L);

        assertEquals(1, total);

        verify(accountRepository,times(3)).findById(1L);
        verify(accountRepository, times(3)).findById(2L);
        verify(accountRepository,times(2)).save(any(Account.class));

        verify(bankRepository,times(2)).findById(anyLong());
        verify(bankRepository).save(any(Bank.class));

        verify(accountRepository,times(6)).findById(any());
        verify(accountRepository, never()).findAll();
    }

    @Test
    void testTransferAmountInsufficientFunds() {
        when(accountRepository.findById(1L)).thenReturn(Data.createAccount001());
        when(accountRepository.findById(2L)).thenReturn(Data.createAccount002());
        when(bankRepository.findById(1L)).thenReturn(Data.createBank());


        BigDecimal originAccountBalance = accountService.checkBalance(1L);
        BigDecimal destinyAccountBalance = accountService.checkBalance(2L);

        assertEquals("1000", originAccountBalance.toPlainString());
        assertEquals("2000", destinyAccountBalance.toPlainString());

        var exception = assertThrows(InsufficientFundsException.class, ()->{
        accountService.transfer(1L,2L,new BigDecimal("1200"), 1L);

        });

        originAccountBalance = accountService.checkBalance(1L);
        destinyAccountBalance = accountService.checkBalance(2L);

        assertEquals("1000", originAccountBalance.toPlainString());
        assertEquals("2000", destinyAccountBalance.toPlainString());

        int total = accountService.checkTotalTransfer(1L);

        assertEquals(0, total);

        verify(accountRepository,times(3)).findById(1L);
        verify(accountRepository, times(2)).findById(2L);
        verify(accountRepository,never()).save(any(Account.class));

        verify(bankRepository,times(1)).findById(anyLong());
        verify(bankRepository, never()).save(any(Bank.class));

        verify(accountRepository,times(5)).findById(any());
        verify(accountRepository, never()).findAll();
    }

    @Test
    void TestFindByIdThenReturnAccount001() {


        when(accountRepository.findById(any())).thenReturn(Data.createAccount001());

        Account account1 = accountService.findById(1L);
        Account account2 = accountService.findById(1L);
        assertSame(account1,account2);

        verify(accountRepository,times(2)).findById(1L);
    }
}
