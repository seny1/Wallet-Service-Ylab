package org.elSasen.infrastructure.console.state;

import org.elSasen.infrastructure.console.StateConsole;
import org.elSasen.infrastructure.console.state.client.AuthorizationGeneralState;
import org.elSasen.infrastructure.console.state.client.RegistrationState;

import java.util.Scanner;

/**
 * Класс начального состояния приложения
 */
public class MainState implements StateConsole {

    /**
     * Поле следующего состояния
     */
    private StateConsole nextState;

    /**
     * Метод, отвечающий за начальные взаимодействия пользователя с приложением
     */
    @Override
    public void process() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("What would you like to do? (r - registration, a - authorization, e - exit) ");
        String input = scanner.next();
        switch (input) {
            case "r":
                nextState = new RegistrationState();
                break;
            case "a":
                nextState = new AuthorizationGeneralState();
                break;
            case "e":
                System.exit(0);
            default:
                System.out.println("Invalid input!");
                break;
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
