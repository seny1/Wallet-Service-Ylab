package org.infrastructure.console.state.client;

import org.app.logic.ClientService;
import org.infrastructure.console.StateConsole;

import java.util.Scanner;

/**
 * Класс состояния, отвечающий за регистрацию клиента
 */
public class RegistrationState implements StateConsole {

    /**
     * Поле, позволяющее работать с логикой клиентов
     */
    private final ClientService clientService = ClientService.getInstance();
    /**
     * Поле следующего состояния
     */
    private StateConsole nextState;

    /**
     * Метод, отвечающий за регистрацию клиентов
     */
    @Override
    public void process() {

        System.out.println("-----------REGISTRATION-----------");
        String choose;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (!clientService.registration(login, password)) {
            System.out.println("A user with this login already exists");
            nextState = new RegistrationState();
        } else {
            System.out.print("Would you like to make one more registration?(y/n) ");
            choose = scanner.nextLine();

            switch (choose) {
                case "y":
                    nextState = new RegistrationState();
                    break;
                case "n":
                    nextState = new AuthorizationGeneralState();
                    break;
            }
        }
    }

    /**
     * Метод для перехода в следующее состояние
     * @return Следующее состояние
     */
    @Override
    public StateConsole nextState() {
        return nextState;
    }
}
