package org.infrastructure.in_memory;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.domain.Client;
import org.repository_interface.ClientRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для работы клиентов с памятью приложения
 */
public class ClientInMemory implements ClientRepository {

    /**
     * Словарь для всех клиентов с ключом - логином и значением - самим клиентом
     */
    private final Map<String, Client> clientMap = new HashMap<>();
    /**
     * Словарь для аудита каждого клиента с ключом - логином и значением - действием клиента
     */
    private final MultiValuedMap<String, String> audit = new ArrayListValuedHashMap<>();
    /**
     * Поле для реализации паттерна проектирования Singleton
     */
    private final static ClientRepository INSTANCE = new ClientInMemory();

    /**
     * Проверка клиента по лоигну и паролю
     * @param client Клиент
     * @return Результат проверки
     */
    @Override
    public boolean checkClient(Client client) {
        return clientMap.containsKey(client.getLogin()) && clientMap.get(client.getLogin()).getPassword().equals(client.getPassword());
    }

    /**
     * Добавление клиента в словарь всех клиентов
     * @param client Клиент
     */
    @Override
    public void addClient(Client client) {
        clientMap.put(client.getLogin(), client);
    }

    /**
     * Проверка на то, соодержится ли клиент с определенным логином в словаре
     * @param login Логин
     * @return Резульат проверки
     */
    @Override
    public boolean findClientByLogin(String login) {
        return clientMap.containsKey(login);
    }

    /**
     * Поиск клиента с определенным логином
     * @param login Логин
     * @return Клиент с определенным логином
     */
    @Override
    public Client findClientByLoginReturnClient(String login) {
        return clientMap.get(login);
    }

    /**
     * Добавление действия клиента в аудит
     * @param login Логин
     * @param action Запись (действие клиента)
     */
    @Override
    public void addInAudit(String login, String action) {
        audit.put(login, action);
    }

    /**
     * Вывод аудита клиента с определенным логином
     * @param login Логин
     */
    @Override
    public void printAudit(String login) {
        audit.get(login).forEach(System.out::println);
    }

    /**
     * Метод для реализации паттерна проектирования Singleton
     * @return Экземпляр класса
     */
    public static ClientRepository getInstance() {
        return INSTANCE;
    }
}
