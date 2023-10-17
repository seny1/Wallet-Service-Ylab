package org.elSasen.app.logic;

import lombok.RequiredArgsConstructor;
import org.elSasen.app.service.ClientServiceI;
import org.elSasen.domain.Client;
import org.elSasen.infrastructure.in_memory.ClientInMemory;
import org.elSasen.repository_interface.ClientRepository;

/**
 * Класс, реализующий бизнес-логику клиентов
 */
@RequiredArgsConstructor
public class ClientService implements ClientServiceI {

    /**
     * Поле для взаимодействия с памятью приложения
     */
    private final ClientRepository clientRepository;

    /**
     * Поле для реализации паттерна проектирования Singleton
     */
    private final static ClientService INSTANCE = new ClientService(ClientInMemory.getInstance());


    /**
     * Реализация авторизации клиента по логину и паролю
     * @param login Логин клиента
     * @param password Пароль клиента
     * @return Результат попытки авторизации
     */


    @Override
    public boolean authorization(String login, String password) {
        if (clientRepository.checkClient(new Client(login, password, 0))) {
            clientRepository.addInAudit(login, "authorization");
            return true;
        }
        return false;
    }

    /**
     * Реализация регистрации клиента по логину и паролю
     * @param login Логин клиента
     * @param password Пароль клиента
     * @return Результат попытки регистрации
     */
    @Override
    public boolean registration(String login, String password) {
        if (clientRepository.findClientByLogin(login)) {
            return false;
        } else {
            clientRepository.addClient(new Client(login, password, 0));
            clientRepository.addInAudit(login, "registration");
            return true;
        }
    }

    /**
     * Реализация выхода клиента из аккаунта
     * @param login Логин клиента
     */
    @Override
    public void logout(String login) {
        clientRepository.addInAudit(login, "logout");
    }

    /**
     * Проверка баланска клиента
     * @param login Логин клиента
     * @return Баланс
     */
    @Override
    public int checkBalance(String login) {
        clientRepository.addInAudit(login, "check balance");
        return clientRepository.findClientByLoginReturnClient(login).getBalance();
    }

    /**
     * Вывод аудита клиента
     * @param login Логин клиента
     */
    @Override
    public void printAudit(String login) {
        clientRepository.printAudit(login);
    }

    /**
     * Метод для реализации паттерна проектирования Singleton
     */
    public static ClientService getInstance() {
        return INSTANCE;
    }

}
