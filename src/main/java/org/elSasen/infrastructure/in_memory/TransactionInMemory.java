package org.elSasen.infrastructure.in_memory;

import org.elSasen.domain.Client;
import org.elSasen.domain.Transaction;
import org.elSasen.domain.TypeOfTransaction;
import org.elSasen.repository_interface.TransactionRepository;
import org.elSasen.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс для работы транзакций с памятью приложения
 */
public class TransactionInMemory implements TransactionRepository {

    /**
     * Поле для реализации паттерна проектирования Singleton
     */
    private final static TransactionRepository INSTANCE = new TransactionInMemory();

    /**
     * Проверка на валидность суммы транзакции
     * @param typeOfTransaction Тип транзакции
     * @param amount Сумма транзакции
     * @param client Клиент
     * @return Резульат проверки
     */
    @Override
    public boolean validateAmount(TypeOfTransaction typeOfTransaction, int amount, Client client) {
        if (typeOfTransaction == TypeOfTransaction.Credit) {
            return true;
        }
        String sql = """
                SELECT balance
                FROM entities."client"
                WHERE login = ?
                """;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, client.getLogin());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int balance = resultSet.getInt("balance");
            return balance > amount;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Добавление транзакции в словарь
     * @param type Тип транзакции
     * @param amount Сумма
     */
    @Override
    public Long addTransaction(TypeOfTransaction type, int amount) {
        String sql = """
                INSERT INTO entities."transaction" (type, amount)
                VALUES (?, ?) RETURNING id
                """;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, String.valueOf(type));
            preparedStatement.setInt(2, amount);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getLong("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Добавление транзакции в историю транзакций клиента
     * @param client Клиент
     * @param typeOfTransaction Тип транзакции
     * @param amount Сумма
     */
    @Override
    public int addTransactionHistory(Client client, TypeOfTransaction typeOfTransaction, int amount) {
        String sql = """
                INSERT INTO "transactions_history" (client_login, type_of_transaction, amount)
                VALUES (?, ?, ?) RETURNING id
                """;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, client.getLogin());
            preparedStatement.setString(2, String.valueOf(typeOfTransaction));
            preparedStatement.setInt(3, amount);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Вывод истории транзакций определенного клиента
     * @param client Клиент
     */
    @Override
    public void printHistory(Client client) {
        String sql = """
                SELECT type_of_transaction, amount
                FROM "transactions_history"
                WHERE client_login = ?
                """;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, client.getLogin());
            ResultSet resultSet = preparedStatement.executeQuery();
            int i = 1;
            while (resultSet.next()) {
                String typeOfTransaction = resultSet.getString("type_of_transaction");
                String amount = resultSet.getString("amount");
                System.out.println(i + " " + typeOfTransaction + " " + amount);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для реализации паттерна проектирования Singleton
     * @return Экземпляр класса
     */
    public static TransactionRepository getInstance() {
        return INSTANCE;
    }
}
