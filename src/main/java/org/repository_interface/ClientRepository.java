package org.repository_interface;

import org.domain.Client;
import org.domain.Transaction;

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
    void addClient(Client client);

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
     * Добавление записи в аудит клиента по его логину
     * @param login Логин
     * @param action Запись (действие клиента)
     */
    void addInAudit(String login, String action);

    /**
     * Вывод аудита клиента по его логину
     * @param login Логин
     */
    void printAudit(String login);
}
