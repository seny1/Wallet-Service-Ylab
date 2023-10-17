package org.elSasen.infrastructure.in_memory;

import org.elSasen.domain.Client;
import org.elSasen.repository_interface.ClientRepository;
import org.elSasen.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс для работы клиентов с памятью приложения
 */
public class ClientInMemory implements ClientRepository {

    private final static ClientRepository INSTANCE = new ClientInMemory();

    /**
     * Проверка клиента по лоигну и паролю
     *
     * @param client Клиент
     * @return Результат проверки
     */
    @Override
    public boolean checkClient(Client client) {
        String sql = """
                SELECT id, login, password, balance
                FROM entities."client"
                WHERE login = ? AND password = ?
                """;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, client.getLogin());
            preparedStatement.setString(2, client.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Добавление клиента в словарь всех клиентов
     *
     * @param client Клиент
     */
    @Override
    public Long addClient(Client client) {
        String sql = """
                INSERT INTO entities."client" (login, password, balance)
                VALUES (?, ?, ?) RETURNING id
                """;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, client.getLogin());
            preparedStatement.setString(2, client.getPassword());
            preparedStatement.setInt(3, client.getBalance());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getLong("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Проверка на то, соодержится ли клиент с определенным логином в словаре
     *
     * @param login Логин
     * @return Резульат проверки
     */
    @Override
    public boolean findClientByLogin(String login) {
        String sql = """
                SELECT id, login, password, balance
                FROM entities."client"
                WHERE login = ?
                """;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Поиск клиента с определенным логином
     *
     * @param login Логин
     * @return Клиент с определенным логином
     */
    @Override
    public Client findClientByLoginReturnClient(String login) {
        String sql = """
                SELECT login, password, balance
                FROM entities."client"
                WHERE login = ?
                """;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return new Client(resultSet.getString("login"),
                    resultSet.getString("password"),
                    resultSet.getInt("balance"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Списание средств
     * @param login Логин клиента
     * @param amount Сумма, которую нужно списать
     */
    @Override
    public int minusBalance(String login, int amount) {
        String sql = """
                UPDATE entities."client"
                SET balance = balance - ?
                WHERE login = ?
                RETURNING balance;
                """;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, amount);
            preparedStatement.setString(2, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("balance");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Пополнение средств
     * @param login Логин клиента
     * @param amount Сумма, на которую нужно пополнить
     */
    @Override
    public int plusBalance(String login, int amount) {
        String sql = """
                UPDATE entities."client"
                SET balance = balance + ?
                WHERE login = ?
                RETURNING balance;
                """;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, amount);
            preparedStatement.setString(2, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("balance");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Добавление действия клиента в аудит
     * @param login  Логин
     * @param action Запись (действие клиента)
     */
    @Override
    public int addInAudit(String login, String action) {
        String sql = """
                INSERT INTO "client_audit" (client_login, action)
                VALUES (?, ?)
                RETURNING id
                """;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, action);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Вывод аудита клиента с определенным логином
     * @param login Логин
     */
    @Override
    public void printAudit(String login) {
        String sql = """
                SELECT action
                FROM "client_audit"
                WHERE client_login = ?
                """;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getString("action"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Метод для реализации паттерна проектирования Singleton
     * @return Экземпляр класса
     */
    public static ClientRepository getInstance() {
        return INSTANCE;
    }
}
