package org.elSasen.app.logic;

import lombok.RequiredArgsConstructor;
import org.elSasen.app.service.TransactionServiceI;
import org.elSasen.domain.TypeOfTransaction;
import org.elSasen.repository_interface.ClientRepository;
import org.elSasen.repository_interface.TransactionRepository;
import org.elSasen.infrastructure.in_memory.ClientInMemory;
import org.elSasen.infrastructure.in_memory.TransactionInMemory;

/**
 * Класс, реализующий бизнес-логику транзакций
 */
@RequiredArgsConstructor
public class TransactionService implements TransactionServiceI {

    /**
     * Поле для взаимодействия с памятью приложения
     */
    private final TransactionRepository transactionRepository;
    /**
     * Поле для взаимодействия с памятью приложения
     */
    private final ClientRepository clientRepository;
    /**
     * Поле для реализации паттерна проектирования Singleton
     */
    private final static TransactionService INSTANCE = new TransactionService(TransactionInMemory.getInstance(), ClientInMemory.getInstance());


    /**
     * Реализация снятия средств со счета
     *
     * @param transactionAmount Сумма
     * @param login             Логин клиента
     * @return Результат попытки снятия
     */
    @Override
    public boolean debit(int transactionAmount, String login) {
        if (transactionRepository.validateAmount(TypeOfTransaction.Debit, transactionAmount, clientRepository.findClientByLoginReturnClient(login))) {
            transactionRepository.addTransaction(TypeOfTransaction.Debit, transactionAmount);
            clientRepository.minusBalance(login, transactionAmount);
            transactionRepository.addTransactionHistory(clientRepository.findClientByLoginReturnClient(login), TypeOfTransaction.Debit, transactionAmount);
            clientRepository.addInAudit(login, "debit: " + transactionAmount);
            return true;
        } else {
            System.out.println("Insufficient funds!");
        }
        return false;
    }

    /**
     * Реализация пополнения счета
     * @param transactionAmount Сумма
     * @param login             Логин клиента
     */
    @Override
    public boolean credit(int transactionAmount, String login) {
        transactionRepository.addTransaction(TypeOfTransaction.Credit, transactionAmount);
        clientRepository.plusBalance(login, transactionAmount);
        transactionRepository.addTransactionHistory(clientRepository.findClientByLoginReturnClient(login), TypeOfTransaction.Credit, transactionAmount);
        clientRepository.addInAudit(login, "credit: " + transactionAmount);
        return true;
    }

    /**
     * Вывод истории транзакций по логину
     *
     * @param login Логин клиента
     */
    @Override
    public void printHistory(String login) {
        transactionRepository.printHistory(clientRepository.findClientByLoginReturnClient(login));
        clientRepository.addInAudit(login, "print history");
    }

    /**
     * Метод для реализации паттерна проектирования Singleton
     */
    public static TransactionService getInstance() {
        return INSTANCE;
    }
}
