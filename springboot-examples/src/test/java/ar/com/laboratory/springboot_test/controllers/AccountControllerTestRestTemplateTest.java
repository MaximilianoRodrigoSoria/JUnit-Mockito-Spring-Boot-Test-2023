package ar.com.laboratory.springboot_test.controllers;

import ar.com.laboratory.springboot_test.models.Account;
import ar.com.laboratory.springboot_test.models.TransactionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTestRestTemplateTest {

    @Autowired
    private TestRestTemplate client;

    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testTransferir() throws JsonProcessingException {
        TransactionDto dto = new TransactionDto();
        dto.setAmount(new BigDecimal("100"));
        dto.setAccountDestinyId(2L);
        dto.setAccountOriginId(1L);
        dto.setBankId(1L);

        ResponseEntity<String> response = client.
                postForEntity(crearUri("/api/accounts/transfer"), dto, String.class);
        System.out.println(port);
        String json = response.getBody();
        System.out.println(json);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(json);
        assertTrue(json.contains("Transaction susses!"));
        assertTrue(json.contains("{\"accountOriginId\":1,\"accountDestinyId\":2,\"amount\":100,\"bankId\":1}"));

        JsonNode jsonNode = objectMapper.readTree(json);
        assertEquals("Transaction susses!", jsonNode.path("message").asText());
        assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
        assertEquals("100", jsonNode.path("transaction").path("amount").asText());
        assertEquals(1L, jsonNode.path("transaction").path("accountOriginId").asLong());

        Map<String, Object> response2 = new HashMap<>();
        response2.put("date", LocalDate.now().toString());
        response2.put("status", "OK");
        response2.put("message", "Transaction susses!");
        response2.put("transaction", dto);

        assertEquals(objectMapper.writeValueAsString(response2), json);

    }

    @Test
    @Order(2)
    void testDetalle() {
        ResponseEntity<Account> respuesta = client.getForEntity(crearUri("/api/accounts/1"), Account.class);
        Account account = respuesta.getBody();
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());

        assertNotNull(account);
        assertEquals(1L, account.getId());
        assertEquals("Maximiliano", account.getPerson());
        assertEquals("900.00", account.getBalance().toPlainString());
        assertEquals(new Account(1L, "Maximiliano", new BigDecimal("900.00")), account);
    }

    @Test
    @Order(3)
    void testListar() throws JsonProcessingException {
        ResponseEntity<Account[]> respuesta = client.getForEntity(crearUri("/api/accounts/"), Account[].class);
        List<Account> accounts = Arrays.asList(respuesta.getBody());

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());

        assertEquals(2, accounts.size());
        assertEquals(1L, accounts.get(0).getId());
        assertEquals("Maximiliano", accounts.get(0).getPerson());
        assertEquals("900.00", accounts.get(0).getBalance().toPlainString());
        assertEquals(2L, accounts.get(1).getId());
        assertEquals("John", accounts.get(1).getPerson());
        assertEquals("2100.00", accounts.get(1).getBalance().toPlainString());

        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(accounts));
        assertEquals(1L, json.get(0).path("id").asLong());
        assertEquals("Maximiliano", json.get(0).path("person").asText());
        assertEquals("900.0", json.get(0).path("balance").asText());
        assertEquals(2L, json.get(1).path("id").asLong());
        assertEquals("John", json.get(1).path("person").asText());
        assertEquals("2100.0", json.get(1).path("balance").asText());
    }

    @Test
    @Order(4)
    void testGuardar() {
        Account account = new Account(null, "Pepa", new BigDecimal("3800"));

        ResponseEntity<Account> respuesta = client.postForEntity(crearUri("/api/accounts"), account, Account.class);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());
        Account accountCreated = respuesta.getBody();
        assertNotNull(accountCreated);
        assertEquals(3L, accountCreated.getId());
        assertEquals("Pepa", accountCreated.getPerson());
        assertEquals("3800", accountCreated.getBalance().toPlainString());
    }

    @Test
    @Order(5)
    void testEliminar() {

        ResponseEntity<Account[]> respuesta = client.getForEntity(crearUri("/api/accounts/"), Account[].class);
        List<Account> accounts = Arrays.asList(respuesta.getBody());
        assertEquals(3, accounts.size());

        //client.delete(crearUri("/api/accounts/3"));
        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id", 3L);
        ResponseEntity<Void> exchange = client.exchange(crearUri("/api/accounts/{id}"), HttpMethod.DELETE, null, Void.class,
                pathVariables);

        assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
        assertFalse(exchange.hasBody());

        respuesta = client.getForEntity(crearUri("/api/accounts/"), Account[].class);
        accounts = Arrays.asList(respuesta.getBody());
        assertEquals(2, accounts.size());

        ResponseEntity<Account> responseDetail = client.getForEntity(crearUri("/api/accounts/3"), Account.class);
        assertEquals(HttpStatus.NOT_FOUND, responseDetail.getStatusCode());
        assertFalse(responseDetail.hasBody());
    }

    private String crearUri(String uri) {
        return "http://localhost:" + port + uri;
    }
}
