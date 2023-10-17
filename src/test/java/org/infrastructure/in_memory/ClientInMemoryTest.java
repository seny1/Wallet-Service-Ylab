package org.infrastructure.in_memory;

import org.elSasen.domain.Client;
import org.elSasen.domain.TypeOfTransaction;
import org.elSasen.infrastructure.in_memory.ClientInMemory;
import org.elSasen.util.ConnectionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.*;

@Testcontainers
class ClientInMemoryTest {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15.2")
            .withDatabaseName("Wallet-Service")
            .withUsername("ArsenyRevunov52")
            .withPassword("ArsenyRevunov52");

    private final ClientInMemory clientInMemory = new ClientInMemory();
    private Connection connection;

    @BeforeAll
    static void runContainer() {
        Startables.deepStart(container);
        JdbcDatabaseDelegate jdbcDatabaseDelegate = new JdbcDatabaseDelegate(container, "");
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "initScriptsForTests/create-entities-and-transaction.sql");
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "initScriptsForTests/create-client.sql");
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "initScriptsForTests/insert-data-into-client.sql");
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "initScriptsForTests/create-audit.sql");
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "initScriptsForTests/insert-data-into-audit.sql");
    }

    @BeforeEach
    public void beforeEach() throws SQLException {
        connection = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
    }

    @Test
    void checkClientTest() {
        try (MockedStatic<ConnectionManager> theMock = Mockito.mockStatic(ConnectionManager.class)) {
            theMock.when(ConnectionManager::open).thenReturn(connection);
            Client client = new Client("Ivan", "12345", 500);
            boolean test = clientInMemory.checkClient(client);
            Assertions.assertTrue(test);
        }
    }

    @Test
    void addClientTest() {
        try (MockedStatic<ConnectionManager> theMock = Mockito.mockStatic(ConnectionManager.class)) {
            theMock.when(ConnectionManager::open).thenReturn(connection);
            Client expected = new Client("Petr", "1234", 1000);
            Long id = clientInMemory.addClient(expected);
            String sql = """
                    SELECT login, password, balance
                    FROM entities."client"
                    WHERE id = ?
                    """;
            Connection new_connection = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
            PreparedStatement preparedStatement = new_connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Client actual = new Client(resultSet.getString("login"), resultSet.getString("password"), resultSet.getInt("balance"));
            Assertions.assertEquals(expected, actual);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findClientByLoginTestTrue() {
        try (MockedStatic<ConnectionManager> theMock = Mockito.mockStatic(ConnectionManager.class)) {
            theMock.when(ConnectionManager::open).thenReturn(connection);
            boolean test = clientInMemory.findClientByLogin("Ivan");
            Assertions.assertTrue(test);
        }
    }

    @Test
    void findClientByLoginTestFalse() {
        try (MockedStatic<ConnectionManager> theMock = Mockito.mockStatic(ConnectionManager.class)) {
            theMock.when(ConnectionManager::open).thenReturn(connection);
            boolean test = clientInMemory.findClientByLogin("Yuri");
            Assertions.assertFalse(test);
        }
    }

    @Test
    void findClientByLoginReturnClientTest() {
        try (MockedStatic<ConnectionManager> theMock = Mockito.mockStatic(ConnectionManager.class)) {
            theMock.when(ConnectionManager::open).thenReturn(connection);
            Client actual = clientInMemory.findClientByLoginReturnClient("Serega");
            Assertions.assertEquals("Serega", actual.getLogin());
            Assertions.assertEquals("12345", actual.getPassword());
            Assertions.assertEquals(500, actual.getBalance());
        }
    }

    @Test
    void minusBalanceTest() {
        try (MockedStatic<ConnectionManager> theMock = Mockito.mockStatic(ConnectionManager.class)) {
            theMock.when(ConnectionManager::open).thenReturn(connection);
            int actual = clientInMemory.minusBalance("Ivan", 300);
            int expected = 200;
            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    void plusBalanceTest() {
        try (MockedStatic<ConnectionManager> theMock = Mockito.mockStatic(ConnectionManager.class)) {
            theMock.when(ConnectionManager::open).thenReturn(connection);
            int actual = clientInMemory.plusBalance("Serega", 300);
            int expected = 800;
            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    void addInAuditTest() {
        try (MockedStatic<ConnectionManager> theMock = Mockito.mockStatic(ConnectionManager.class)) {
            theMock.when(ConnectionManager::open).thenReturn(connection);
            int id = clientInMemory.addInAudit("Serega", "registration");
            String sql = """
                    SELECT client_login, action
                    FROM public.client_audit
                    WHERE id = ?
                    """;
            Connection new_connection = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
            PreparedStatement preparedStatement = new_connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Assertions.assertEquals("Serega", resultSet.getString("client_login"));
            Assertions.assertEquals("registration", resultSet.getString("action"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void printAudit() {
        try (MockedStatic<ConnectionManager> theMock = Mockito.mockStatic(ConnectionManager.class)) {
            theMock.when(ConnectionManager::open).thenReturn(connection);
            ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStreamCaptor));
            clientInMemory.printAudit("Ivan");
            Assertions.assertEquals("registration", outputStreamCaptor.toString().trim());
        }
    }
}