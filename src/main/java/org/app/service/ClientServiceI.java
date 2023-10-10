package org.app.service;

/**
 * Интерфейс сервиса, реализующий бизнес-логику клиентов
 */
public interface ClientServiceI {

    /**
     * Попытка клиента авторизоваться
     * @param login Логин клиента
     * @param password Пароль клиента
     * @return Результат попытки
     */
    boolean authorization(String login, String password);

    /**
     * Попытка клиента зарегистрироваться
     * @param login Логин клиента
     * @param password Пароль клиента
     * @return Результат попытки
     */
    boolean registration(String login, String password);

    /**
     * Выход клиента из аккаунта
     * @param login Логин клиента
     */
    void logout(String login);

    /**
     * Проверка баланса
     * @param login Логин клиента
     * @return Баланс
     */
    int checkBalance(String login);

    /**
     * Вывод аудита клиента
     * @param login Логин клиента
     */
    void printAudit(String login);
}
