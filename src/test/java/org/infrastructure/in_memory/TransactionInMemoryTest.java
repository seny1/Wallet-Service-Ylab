package org.infrastructure.in_memory;

import org.elSasen.domain.Client;
import org.elSasen.domain.Transaction;
import org.elSasen.domain.TypeOfTransaction;
import org.elSasen.infrastructure.in_memory.TransactionInMemory;
import org.elSasen.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.*;
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
class TransactionInMemoryTest {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15.2")
            .withDatabaseName("Wallet-Service")
            .withUsername("ArsenyRevunov52")
            .withPassword("ArsenyRevunov52");

    private final TransactionInMemory transactionInMemory = new TransactionInMemory();

    private Connection connection;

    @BeforeAll
    static void runContainer() {
        Startables.deepStart(container);
        JdbcDatabaseDelegate jdbcDatabaseDelegate = new JdbcDatabaseDelegate(container, "");
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "initScriptsForTests/create-entities-and-transaction.sql");
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "initScriptsForTests/create-client.sql");
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "initScriptsForTests/insert-data-into-client.sql");
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "initScriptsForTests/create-history.sql");
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "initScriptsForTests/insert-data-into-history.sql");
    }

    @BeforeEach
    public void beforeEach() throws SQLException {
        connection = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
    }


    @Test
    void validateAmountTestTrue() {
        try (MockedStatic<ConnectionManager> theMock = Mockito.mockStatic(ConnectionManager.class)) {
            theMock.when(ConnectionManager::open).thenReturn(connection);
            Client client = new Client("Ivan", "12345", 500);
            boolean test = transactionInMemory.validateAmount(TypeOfTransaction.Debit, 400, client);
            Assertions.assertTrue(test);
        }
    }

    @Test
    void validateAmountTestFalse() {
        try (MockedStatic<ConnectionManager> theMock = Mockito.mockStatic(ConnectionManager.class)) {
            theMock.when(ConnectionManager::open).thenReturn(connection);
            Client client = new Client("Ivan", "12345", 5000);
            boolean test = transactionInMemory.validateAmount(TypeOfTransaction.Debit, 600, client);
            Assertions.assertFalse(test);
        }
    }

    @Test
    void addTransactionTest() {
        try (MockedStatic<ConnectionManager> theMock = Mockito.mockStatic(ConnectionManager.class)) {
            theMock.when(ConnectionManager::open).thenReturn(connection);
            Long id = transactionInMemory.addTransaction(TypeOfTransaction.Debit, 500);
            String sql = """
                    SELECT id, type, amount
                    FROM entities."transaction"
                    WHERE id = ?
                    """;
            Connection new_connection = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
            PreparedStatement preparedStatement = new_connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Transaction expected = new Transaction(id, TypeOfTransaction.Debit, 500);
            Transaction actual = new Transaction(id, TypeOfTransaction.valueOf(resultSet.getString("type")), resultSet.getInt("amount"));
            Assertions.assertEquals(expected, actual);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addTransactionHistoryTest() {
        try (MockedStatic<ConnectionManager> theMock = Mockito.mockStatic(ConnectionManager.class)) {
            theMock.when(ConnectionManager::open).thenReturn(connection);
            int id = transactionInMemory.addTransactionHistory(new Client("Ivan", "123", 500), TypeOfTransaction.Debit, 10);
            String sql = """
                    SELECT client_login, type_of_transaction, amount
                    FROM public.transactions_history
                    WHERE id = ?
                    """;
            Connection new_connection = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
            PreparedStatement preparedStatement = new_connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Assertions.assertEquals("Ivan", resultSet.getString("client_login"));
            Assertions.assertEquals(TypeOfTransaction.Debit.name(), resultSet.getString("type_of_transaction"));
            Assertions.assertEquals(10, resultSet.getInt("amount"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void printHistoryTest() {
        try (MockedStatic<ConnectionManager> theMock = Mockito.mockStatic(ConnectionManager.class)) {
            theMock.when(ConnectionManager::open).thenReturn(connection);
            ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStreamCaptor));
            transactionInMemory.printHistory(new Client("Ivan", "123", 500));
            Assertions.assertEquals("1 Credit 700", outputStreamCaptor.toString().trim());
        }
    }
}
