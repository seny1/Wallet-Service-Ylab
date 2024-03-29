package org.elSasen.repository_interface;

import org.elSasen.domain.Client;

/**
 * Интерфейс, предоставляющий методы для работы с клиентами
 */
public interface ClientRepository {
    /**
     * Метод, проверяющий клиента
     * @param client Клиент
     * @return Проверка клиента
     */
    boolean checkClient(Client client);

    /**
     * Добавление клиента в память
     * @param client Клиент
     */
    Long addClient(Client client);

    /**
     * Поиск клиента по его логину
     * @param login Логин
     * @return Существует ли клиент с таким логином
     */
    boolean findClientByLogin(String login);

    /**
     * Поиск клиента по его логину
     * @param login Логин
     * @return Клиент с определенным логином
     */
    Client findClientByLoginReturnClient(String login);

    /**
     * Метод, списывающий деньгий с баланса
     * @param amount Сумма, которую нужно списать
     */
    int minusBalance(String login, int amount);

    /**
     * Метод, позволящий положить деньги на баланс
     * @param amount Сумма, которую нужно положить
     */
    int plusBalance(String login, int amount);

    /**
     * Добавление записи в аудит клиента по его логину
     * @param login Логин
     * @param action Запись (действие клиента)
     */
    int addInAudit(String login, String action);

    /**
     * Вывод аудита клиента по его логину
     * @param login Логин
     */
    void printAudit(String login);
}
