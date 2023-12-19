package ar.com.laboratory.springboot_test.controllers;


import ar.com.laboratory.springboot_test.models.Account;
import ar.com.laboratory.springboot_test.models.TransactionDto;
import ar.com.laboratory.springboot_test.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ar.com.laboratory.springboot_test.Data.createAccount001;
import static ar.com.laboratory.springboot_test.Data.createAccount002;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService accountService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void getById() throws Exception {
        //Assemble
        when(accountService.findById(1L)).thenReturn(createAccount001().orElseThrow());

        //Act
        mvc.perform(get("/api/accounts/1")
                .contentType(MediaType.APPLICATION_JSON))
        //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person").value("Maximiliano"))
                .andExpect(jsonPath("$.balance").value("1000"));

        verify(accountService).findById(1L);
    }

    @Test
    void testTransfer() throws Exception {
        //Assemble
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAccountOriginId(1L);
        transactionDto.setAccountDestinyId(2L);
        transactionDto.setAmount(new BigDecimal("100"));
        transactionDto.setBankId(1L);

        //Act
        mvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDto))
        //Assert
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Transaction susses!"))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.transaction.accountOriginId").value(1L))
                .andExpect(jsonPath("$.status").value("OK"))
                ;
    }

    @Test
    void testList ()throws Exception {
        List<Account> accounts = Arrays.asList(createAccount001().orElseThrow(), createAccount002().orElseThrow());

        when(accountService.findAll()).thenReturn(accounts);
        mvc.perform(get("/api/accounts/")
                                .contentType(MediaType.APPLICATION_JSON)

                        //Assert
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].person").value("Maximiliano"))
                .andExpect(jsonPath("$[0].balance").value("1000"))
                .andExpect(jsonPath("$[1].person").value("Jhon"))
                .andExpect(jsonPath("$[1].balance").value("2000"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(accounts)))

        ;
    }
    @Test
    void testSave ()throws Exception  {
        //Assemble
        var account = new Account(null, "Pepe", new BigDecimal("3000"));
        when(accountService.save(any(Account.class))).then(invocationOnMock -> {
            Account c = invocationOnMock.getArgument(0);
            c.setId(3L);
            return c;
        });
        //Act
        mvc.perform(post("/api/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(account))
                        //Assert
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.person").value("Pepe"))
                .andExpect(jsonPath("$.balance").value(3000))
        ;
        verify(accountService).save(any());
    }
    @Test
    void testContentResponse() throws Exception {
        //Assemble
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAccountOriginId(1L);
        transactionDto.setAccountDestinyId(2L);
        transactionDto.setAmount(new BigDecimal("100"));
        transactionDto.setBankId(1L);
        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Transaction susses!");
        response.put("transaction", transactionDto);

        System.out.println(objectMapper.writeValueAsString(response));

        //Act
        mvc.perform(post("/api/accounts/transfer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(transactionDto))
                        //Assert
                )
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.message").value("Transaction susses!"))
                                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                                .andExpect(jsonPath("$.transaction.accountOriginId").value(1L))
                                .andExpect(jsonPath("$.status").value("OK"))
                                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                ;
    }




}
