package ar.com.laboratory.springboot_test.controllers;

import ar.com.laboratory.springboot_test.models.Account;
import ar.com.laboratory.springboot_test.models.TransactionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class AccountControllerWebTestClientTests {

    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testTransferir() throws JsonProcessingException {
        // given
        TransactionDto dto = new TransactionDto();
        dto.setAccountOriginId(1L);
        dto.setAccountDestinyId(2L);
        dto.setBankId(1L);
        dto.setAmount(new BigDecimal("100"));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Transaction susses!");
        response.put("transaction", dto);

        // when
        client.post().uri("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                // then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(respuesta -> {
                    try {
                        JsonNode json = objectMapper.readTree(respuesta.getResponseBody());
                        assertEquals("Transaction susses!", json.path("message").asText());
                        assertEquals(1L, json.path("transaction").path("accountOriginId").asLong());
                        assertEquals(LocalDate.now().toString(), json.path("date").asText());
                        assertEquals("100", json.path("transaction").path("amount").asText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.message").value(is("Transaction susses!"))
                .jsonPath("$.message").value(valor -> assertEquals("Transaction susses!", valor))
                .jsonPath("$.message").isEqualTo("Transaction susses!")
                .jsonPath("$.transaction.accountOriginId").isEqualTo(dto.getAccountOriginId())
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                .json(objectMapper.writeValueAsString(response));

    }

    @Test
    @Order(2)
    void testDetalle() throws JsonProcessingException {

        Account account = new Account(1L, "Maximiliano", new BigDecimal("900"));

        client.get().uri("/api/accounts/1").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.person").isEqualTo("Maximiliano")
                .jsonPath("$.balance").isEqualTo(900)
                .json(objectMapper.writeValueAsString(account));
    }

    @Test
    @Order(3)
    void testDetalle2() {

        client.get().uri("/api/accounts/2").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class)
                .consumeWith(response -> {
                    Account account = response.getResponseBody();
                    assertNotNull(account);
                    assertEquals("John", account.getPerson());
                    assertEquals("2100.00", account.getBalance().toPlainString());
                });
    }

    @Test
    @Order(4)
    void testListar() {
        client.get().uri("/api/accounts/").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].person").isEqualTo("Maximiliano")
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].balance").isEqualTo(900)
                .jsonPath("$[1].person").isEqualTo("John")
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].balance").isEqualTo(2100)
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(2));
    }

    @Test
    @Order(5)
    void testListar2() {
        client.get().uri("/api/accounts/").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Account.class)
        .consumeWith(response -> {
            List<Account> accounts = response.getResponseBody();
            assertNotNull(accounts);
            assertEquals(2, accounts.size());
            assertEquals(1L, accounts.get(0).getId());
            assertEquals("Maximiliano", accounts.get(0).getPerson());
            assertEquals(900, accounts.get(0).getBalance().intValue());
            assertEquals(2L, accounts.get(1).getId());
            assertEquals("John", accounts.get(1).getPerson());
            assertEquals("2100.00", accounts.get(1).getBalance().toPlainString());
        })
        .hasSize(2)
        .value(hasSize(2));
    }

    @Test
    @Order(6)
    void testGuardar() {
        // given
        Account account = new Account(null, "Pepe", new BigDecimal("3000"));

        // when
        client.post().uri("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(account)
                .exchange()
        // then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.person").isEqualTo("Pepe")
                .jsonPath("$.person").value(is("Pepe"))
                .jsonPath("$.balance").isEqualTo(3000);
    }

    @Test
    @Order(7)
    void testGuardar2() {
        // given
        Account account = new Account(null, "Pepa", new BigDecimal("3500"));

        // when
        client.post().uri("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(account)
                .exchange()
                // then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class)
        .consumeWith(response -> {
            Account c = response.getResponseBody();
            assertNotNull(c);
            assertEquals(4L, c.getId());
            assertEquals("Pepa", c.getPerson());
            assertEquals("3500", c.getBalance().toPlainString());
        });
    }

    @Test
    @Order(8)
    void testEliminar() {
        client.get().uri("/api/accounts/").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Account.class)
                .hasSize(4);

        client.delete().uri("/api/accounts/3")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        client.get().uri("/api/accounts/").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Account.class)
                .hasSize(3);

        client.get().uri("/api/accounts/3").exchange()
          //.expectStatus().is5xxServerError()
            .expectStatus().isNotFound()
            .expectBody().isEmpty()
        ;
    }
}
