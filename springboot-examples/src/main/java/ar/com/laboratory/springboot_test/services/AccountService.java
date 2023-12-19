package ar.com.laboratory.springboot_test.services;



import ar.com.laboratory.springboot_test.models.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Account findById(Long id);

    int checkTotalTransfer(Long bankId);

    BigDecimal checkBalance(Long accountId);

    void transfer(Long numAccountOrigin, Long numAccountDestiny, BigDecimal amount,
                    Long bankId);

    List<Account> findAll();

    Account save(Account account);

    void deleteById(Long id);
}
