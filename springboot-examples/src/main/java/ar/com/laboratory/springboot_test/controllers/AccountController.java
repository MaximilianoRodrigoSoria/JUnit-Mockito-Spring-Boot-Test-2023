package ar.com.laboratory.springboot_test.controllers;

import ar.com.laboratory.springboot_test.models.Account;
import ar.com.laboratory.springboot_test.models.TransactionDto;
import ar.com.laboratory.springboot_test.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    @ResponseStatus(OK)
    public List<Account> getAll() {
        return accountService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public ResponseEntity<?> getByID(@PathVariable Long id) {
        Account account = null;
        try {
            account = accountService.findById(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Account save(@RequestBody Account cuenta) {
        return accountService.save(cuenta);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransactionDto dto) {
        accountService.transfer(dto.getAccountOriginId(),
                dto.getAccountDestinyId(),
                dto.getAmount(), dto.getBankId());

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Transaction susses!");
        response.put("transaction", dto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        accountService.deleteById(id);
    }

}
