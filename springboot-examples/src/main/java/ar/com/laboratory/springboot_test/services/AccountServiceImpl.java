package ar.com.laboratory.springboot_test.services;

import ar.com.laboratory.springboot_test.models.Account;
import ar.com.laboratory.springboot_test.models.Bank;
import ar.com.laboratory.springboot_test.repositories.AccountRepository;
import ar.com.laboratory.springboot_test.repositories.BankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private BankRepository bankRepository;


    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public int checkTotalTransfer(Long bankId) {
        Bank bank = bankRepository.findById(bankId).orElseThrow();
        return bank.getTotalTransfers();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal checkBalance(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        return account.getBalance();
    }

    @Override
    @Transactional
    public void transfer(Long numAccountOrigin, Long numAccountDestiny, BigDecimal amount, Long bankId) {
         Account accountOrigin = accountRepository.findById(numAccountOrigin).orElseThrow();
         accountOrigin.debit(amount);
         accountRepository.save(accountOrigin);

         Account accountDestiny = accountRepository.findById(numAccountDestiny).orElseThrow();
         accountDestiny.credit(amount);
         accountRepository.save(accountDestiny);

         Bank bank = bankRepository.findById(bankId).orElseThrow();
         int totalTransfers = bank.getTotalTransfers();
         bank.setTotalTransfers(++totalTransfers);
         bankRepository.save(bank);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }
}
