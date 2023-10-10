package org.infrastructure.console.state.client;

import org.app.logic.ClientService;
import org.app.logic.TransactionService;
import org.infrastructure.console.StateConsole;
import org.infrastructure.console.state.MainState;

import java.util.Scanner;

/**
 * Класс состояния, отвечающей за авторизацию и все взаимодействия пользователя с приложением
 */
public class AuthorizationGeneralState implements StateConsole {

    /**
     * Поле, позволяющее работать с логикой клиентов
     */
    private final ClientService clientService = ClientService.getInstance();
    /**
     * Поле, позволяющее работать с логикой транзакций
     */
    private final TransactionService transactionService = TransactionService.getInstance();
    /**
     * Поле следующего состояния
     */
    private StateConsole nextState;
    /**
     * Поле логина для сохранения состояния авторизации
     */
    private String authorizationLogin = " ";
    /**
     * Поле пароля для сохранения состояния авторизации
     */
    private String authorizationPassword = " ";
    /**
     * Флаг состояния клиента (авторизован/не авторизован)
     */
    private boolean authorized = false;

    /**
     * Метод, отвечающий за авторизацию и все взаимодействия пользователя с приложением
     */
    @Override
    public void process() {

        Scanner scanner = new Scanner(System.in);

        if (!authorized) {
            System.out.println("-----------AUTHORIZATION-----------");
            System.out.print("Login: ");
            authorizationLogin = scanner.nextLine();
            System.out.print("Password: ");
            authorizationPassword = scanner.nextLine();
            authorized = clientService.authorization(authorizationLogin, authorizationPassword);
        }

        if (authorized) {
            System.out.print("What do you want to do?(out - logout, chb - check balance, deb - debit, cr - credit, h - check history, a - print audit) ");
            String generalChoose = scanner.nextLine();
            switch (generalChoose) {
                case "out":
                    nextState = new MainState();
                    clientService.logout(authorizationLogin);
                    break;
                case "chb":
                    System.out.println("Balance: " + clientService.checkBalance(authorizationLogin));
                    break;
                case "deb":
                    System.out.print("Enter transaction id: ");
                    int transactionId = scanner.nextInt();
                    System.out.print("Enter amount: ");
                    int transactionAmount = scanner.nextInt();
                    if (transactionService.debit(transactionId, transactionAmount, authorizationLogin)) {
                        System.out.println("Thank you!");
                    } else {
                        System.out.println("Error! Try again");
                    }
                    break;
                case "cr":
                    System.out.print("Enter transaction id: ");
                    int transactionId2 = scanner.nextInt();
                    System.out.print("Enter amount: ");
                    int transactionAmount2 = scanner.nextInt();
                    if (transactionService.credit(transactionId2, transactionAmount2, authorizationLogin)) {
                        System.out.println("Thank you!");
                    } else {
                        System.out.println("Error! Try again");
                    }
                    break;
                case "h":
                    System.out.println("Your transaction's history:");
                    transactionService.printHistory(authorizationLogin);
                    break;
                case "a":
                    System.out.println("Your audit:");
                    clientService.printAudit(authorizationLogin);
                    break;
                default:
                    System.out.println("Invalid input");
            }
        } else {
            System.out.println("Try again");
            nextState = new AuthorizationGeneralState();
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
