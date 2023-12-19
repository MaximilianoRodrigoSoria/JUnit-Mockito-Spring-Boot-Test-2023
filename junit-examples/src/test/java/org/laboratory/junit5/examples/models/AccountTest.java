package org.laboratory.junit5.examples.models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.laboratory.junit5.examples.utils.exceptions.InsufficientBalanceException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class AccountTest {

    Account account;


    @BeforeEach
    void initMethodTest(TestInfo testInfo){
        System.out.println("...Initialized method ".concat(testInfo.getTestMethod().get().getName()).concat("..."));this.account = new Account("Maxi", new BigDecimal("1000.123456"));
    }

    @AfterEach
    void afterEach(TestInfo testInfo) {
        System.out.println("...Finalized method ".concat(testInfo.getTestMethod().get().getName()).concat("..."));
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("...Initialized AccountTest...");

    }

    @AfterAll
    static void afterAll() {
        System.out.println("...Finalized AccountTest...");
    }

    @Test
    @DisplayName("Test name account")
    void testPersonAccount() {
        String expected = "Maxi";
        String result = "";
        result = account.getPerson();
        assertEquals(expected,result);
        assertTrue(result.equals("Maxi"));

    }

    @Test
    @DisplayName("TEST getBalance WHEN name exists THEN status OK")
    void  testBalanceAccount(){

        //Assemble
        var expected = new BigDecimal("1000.123456");
        BigDecimal result;

        //Act
        result = this.account.getBalance();

        // Assert
        assertEquals(expected,result);
        assertFalse(result.compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    @DisplayName("TEST compare accounts WHEN are equals THEN status OK")
    void testAccountReference() {

        //Assemble
        var account1 = new Account("John Doe", new BigDecimal("8900.9997"));
        var account2 = new Account("John Doe", new BigDecimal("8900.9997"));

        //Act and Assert
        assertEquals(account2,account1);
    }


    @Test
    @DisplayName("TEST debit WHEN the balance decreases THEN status OK")
    void testDebitAccount() {
        //Assemble
        var account = new Account("John Doe", new BigDecimal("8900.9997"));

        //Act
        account.debit(new BigDecimal(100));

        //Assert
        assertNotNull(account.getBalance());
        assertEquals(8800, account.getBalance().intValue());
        assertEquals( "8800.9997", account.getBalance().toPlainString());
    }

    @Test
    @DisplayName("TEST credit WHEN the balance increases THEN status OK")
    void testCreditAccount() {
        //Assemble
        var account = new Account("John Doe", new BigDecimal("8900.9997"));

        //Act
        account.credit(new BigDecimal(100));

        //Assert
        assertNotNull(account.getBalance());
        assertEquals(9000, account.getBalance().intValue());
        assertEquals( "9000.9997", account.getBalance().toPlainString());

    }

    @Test
    @DisplayName("TEST debit WHEN the balance is insufficient THEN status Error")
    void testInsufficientBalanceException() {
        //Assemble
        var account = new Account("John Doe", new BigDecimal("8900.9997"));
        //Act and Assert
        var exception = assertThrows(InsufficientBalanceException.class,()-> account.debit(new BigDecimal("8901.9997")));
        String result = exception.getMessage();
        assertEquals("Insufficient balance", result);
    }
    @Test
    @DisplayName("TEST debit WHEN the balance is insufficient THEN status Error")
    void testInsufficientBalanceInjectionException(TestInfo testInfo, TestReporter reporter) {

        System.out.println(testInfo.getDisplayName());
        //Assemble
        var account = new Account("John Doe", new BigDecimal("8900.9997"));
        //Act and Assert
        var exception = assertThrows(InsufficientBalanceException.class,()-> account.debit(new BigDecimal("8901.9997")));
        String result = exception.getMessage();
        assertEquals("Insufficient balance", result);
    }

    @Test
    @DisplayName("TEST transfer WHEN the balance transfer between accounts THEN status OK")
    void testTransferAmountAccount() {
        //Assemble
        var account1 = new Account("John Doe", new BigDecimal("8900.9997"));
        var account2 = new Account("Maxi Soria", new BigDecimal("1500.9997"));
        var bank = new Bank();
        bank.setName("BBVA");

        //Act
        bank.transfer(account1,account2,new BigDecimal(500));

        //Assert
        assertEquals("2000.9997", account2.getBalance().toPlainString());

    }

    @Test
    @Timeout(value = 500, unit = TimeUnit.SECONDS)
    void timeoutTest() throws InterruptedException{
        TimeUnit.SECONDS.sleep(4);
    }

    @ParameterizedTest(name = "number {index} execute with value {0} - {argumentsWithNames}")
    @ValueSource(strings= {"100","200","300","500","600","700","1000"})
    @DisplayName("TEST debit WHEN the balance decreases THEN status OK")
    void testParametrizedDebitAccount(String amount) {
        //Assemble
        var account = new Account("John Doe", new BigDecimal("8900.9997"));

        //Act
        account.debit(new BigDecimal(amount));

        //Assert
        assertNotNull(account.getBalance());
        assertTrue(account.getBalance().compareTo(BigDecimal.ZERO)>0);
    }
    @ParameterizedTest(name = "number {index} execute with value {0} - {argumentsWithNames}")
    @CsvSource({"1,100","2,200","3,300","4,500","5,600","6,700","7,1000"})
    @DisplayName("TEST debit WHEN the balance decreases THEN status OK")
    void testParametrizedValueCSVSource(String index, String amount) {
        System.out.println(index +"->"+ amount);
        //Assemble
        var account = new Account("John Doe", new BigDecimal("8900.9997"));

        //Act
        account.debit(new BigDecimal(amount));

        //Assert
        assertNotNull(account.getBalance());
        assertTrue(account.getBalance().compareTo(BigDecimal.ZERO)>0);
    }
    @ParameterizedTest(name = "number {index} execute with value {0} - {argumentsWithNames}")
    @CsvSource({"101,100","202,200","303,300","4000,500","5000,600","6000,700","7000,1000"})
    @DisplayName("TEST debit WHEN the balance decreases THEN status OK")
    void testParametrizedValueCSVSource2(String balance, String amount) {
        System.out.println(balance +"->"+ amount);
        //Assemble
        var account = new Account("John Doe", new BigDecimal("8900.9997"));

        //Act
        account.setBalance(new BigDecimal(balance));
        account.debit(new BigDecimal(amount));

        //Assert
        assertNotNull(account.getBalance());
        assertTrue(account.getBalance().compareTo(BigDecimal.ZERO)>0);
    }

    @ParameterizedTest(name = "number {index} execute with value {0} - {argumentsWithNames}")
    @CsvFileSource(resources ="/data.csv" )
    @DisplayName("TEST debit WHEN the balance decreases THEN status OK")
    void testParametrizedFileCSVSource(String amount) {
        //Assemble
        var account = new Account("John Doe", new BigDecimal("8900.9997"));

        //Act
        account.debit(new BigDecimal(amount));

        //Assert
        assertNotNull(account.getBalance());
        assertTrue(account.getBalance().compareTo(BigDecimal.ZERO)>0);
    }

    @ParameterizedTest(name = "number {index} execute with value {0} - {argumentsWithNames}")
    @CsvFileSource(resources ="/data2.csv" )
    @DisplayName("TEST debit WHEN the balance decreases THEN status OK")
    void testParametrizedFileCSVSource2(String balance, String amount) {
        //Assemble
        var account = new Account("John Doe", new BigDecimal("8900.9997"));

        //Act
        account.setBalance(new BigDecimal(balance));
        account.debit(new BigDecimal(amount));

        //Assert
        assertNotNull(account.getBalance());
        assertTrue(account.getBalance().compareTo(BigDecimal.ZERO)>0);
    }

    @ParameterizedTest(name = "number {index} execute with value {0} - {argumentsWithNames}")
    @MethodSource("amountList")
    @DisplayName("TEST debit WHEN the balance decreases THEN status OK")
    void testParametrizedMethodSource(String amount) {
        //Assemble
        var account = new Account("John Doe", new BigDecimal("8900.9997"));

        //Act
        account.debit(new BigDecimal(amount));

        //Assert
        assertNotNull(account.getBalance());
        assertTrue(account.getBalance().compareTo(BigDecimal.ZERO)>0);
    }

    @Test
    @DisplayName("TEST assertAll WHEN try multiple tests THEN status OK")
    void testBankAccountsRelationship() {
        var account1 = new Account("John Doe", new BigDecimal("8900.9997"));
        var account2 = new Account("Maxi Soria", new BigDecimal("1500.9997"));
        var bank = new Bank();
        bank.setName("BBVA");
        bank.addAccount(account1);
        bank.addAccount(account2);
        bank.transfer(account1,account2,new BigDecimal(500));
        assertAll(
                ()-> assertEquals("2000.9997", account2.getBalance().toPlainString(),()->"Expected 2000.9997 in balance"),
                ()-> assertEquals(2, bank.getAccounts().size(), ()->"Expected 2 accounts"),
                ()-> assertEquals("BBVA", account1.getBank().getName(),()->"Expected bank name BBVA"),
                ()-> assertEquals("Maxi Soria", bank.getAccounts()
                        .stream()
                        .filter(a->a.getPerson().equals("Maxi Soria"))
                        .findFirst()
                        .get()
                        .getPerson(), ()->"Expected Maxi Soria"),
                ()-> assertTrue(bank.getAccounts()
                        .stream()
                        .filter(a->a.getPerson().equals("Maxi Soria"))
                        .findFirst()
                        .isPresent(),()->"Expected Maxi Soria exist"),
                ()-> assertTrue(bank.getAccounts()
                        .stream()
                        .anyMatch(a -> a.getPerson().equals("Maxi Soria")),()->"Expected Maxi Soria")
                );


    }

    @Nested
    @Tag("operating_systems")
class SystemTests{
    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testOnWindows(){}
    @Test
    @EnabledOnOs({OS.LINUX,OS.MAC})
    void testOnLinuxAndMac(){}
    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testOnNotWindows(){}
}

@Nested
class JavaVersionTests{  @Test
@EnabledOnJre(JRE.JAVA_8)
void testOnlyJRE(){}}

@Nested
class SystemPropertiesTests{

    @Test
    void printSystemProperties(){
        Properties properties = System.getProperties();
        properties.forEach((k,v)-> System.out.println(k+":"+v));

    }

    @Test
    @EnabledIfSystemProperty(named = "sun.arch.data.model", matches = "64")
    void  testModel64(){}

    @Test
    @EnabledIfSystemProperty(named = "ENV", matches = "dev")
    void  testENVDev(){}
}


@Nested
@DisplayName("Environments test ")
class EnvironmentTests {
    @Test
    void printEnvironment() {
        Map<String, String> envs = System.getenv();
        envs.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "PROCESSOR_REVISION", matches = "2100")
    void testEnvironment() {
    }


    @Test
    @DisplayName("TEST getBalance only dev WHEN name exists THEN status OK")
    void testBalanceAccountDev() {

        //Assemble
        boolean isDev = "DEV".equals(System.getProperty("ENV"));
        var expected = new BigDecimal("1000.123456");
        BigDecimal result;

        //Act
        assumeTrue(isDev);
        var account = new Account("Maxi", new BigDecimal("1000.123456"));
        result = account.getBalance();

        // Assert
        assertEquals(expected, result);
        assertFalse(result.compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    @DisplayName("TEST getBalance only dev2 WHEN name exists THEN status OK")
    void testBalanceAccountDev2() {

        //Assemble
        boolean isDev = "DEV".equals(System.getProperty("ENV"));
        var expected = new BigDecimal("1000.123456");
        var account = new Account("Maxi", new BigDecimal("1000.123456"));

        //Act
        assumingThat(isDev, () -> {
            var result = account.getBalance();

            // Assert
            assertEquals(expected, result);
            assertFalse(result.compareTo(BigDecimal.ZERO) < 0);
        });
    }


    @RepeatedTest(value = 5, name = "TEST getBalance only dev WHEN current repetition {currentRepetition} / {totalRepetitions} THEN status OK")
    void testBalanceAccountRepeat(RepetitionInfo repetitionInfo) {
        if(repetitionInfo.getCurrentRepetition() == 3){
            System.out.println("Esta es la 3");
        }

        //Assemble
        boolean isDev = "DEV".equals(System.getProperty("ENV"));
        var expected = new BigDecimal("1000.123456");
        var account = new Account("Maxi", new BigDecimal("1000.123456"));

        //Act
        assumingThat(isDev, () -> {
            var result = account.getBalance();

            // Assert
            assertEquals(expected, result);
            assertFalse(result.compareTo(BigDecimal.ZERO) < 0);
        });
    }
  }

  static List<String> amountList(){
        return List.of("100","200","300","400","500","600","700","1000");
  }
}
