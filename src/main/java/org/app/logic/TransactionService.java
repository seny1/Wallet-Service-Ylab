package org.app.logic;

import lombok.RequiredArgsConstructor;
import org.app.service.TransactionServiceI;
import org.domain.Client;
import org.domain.Transaction;
import org.domain.TypeOfTransaction;
import org.infrastructure.in_memory.ClientInMemory;
import org.infrastructure.in_memory.TransactionInMemory;
import org.repository_interface.ClientRepository;
import org.repository_interface.TransactionRepository;

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
     * @param transactionId Уникальный номер транзакции
     * @param transactionAmount Сумма
     * @param login Логин клиента
     * @return Результат попытки снятия
     */
    @Override
    public boolean debit(int transactionId, int transactionAmount, String login) {
        Transaction transaction = new Transaction(transactionId, TypeOfTransaction.Debit, transactionAmount);
        Client client = clientRepository.findClientByLoginReturnClient(login);
        if (transactionRepository.validateId(transactionId) && transactionRepository.validateAmount(TypeOfTransaction.Debit, transactionAmount, client)) {
            transactionRepository.addTransaction(transaction);
            Client newBalanceClient = new Client(client.getLogin(), client.getPassword(), client.getBalance() - transactionAmount);
            clientRepository.addClient(newBalanceClient);
            transactionRepository.addTransactionHistory(newBalanceClient, transaction);
            clientRepository.addInAudit(login, "debit: " + transactionAmount);
            return true;
        }
        return false;
    }

    /**
     * Реализация пополнения счета
     * @param transactionId Уникальный номер транзакции
     * @param transactionAmount Сумма
     * @param login Логин клиента
     * @return Результат попытки пополнения
     */
    @Override
    public boolean credit(int transactionId, int transactionAmount, String login) {
        Transaction transaction = new Transaction(transactionId, TypeOfTransaction.Credit, transactionAmount);
        Client client = clientRepository.findClientByLoginReturnClient(login);
        if (transactionRepository.validateId(transactionId)) {
            transactionRepository.addTransaction(transaction);
            Client newBalanceClient = new Client(client.getLogin(), client.getPassword(), client.getBalance() + transactionAmount);
            clientRepository.addClient(newBalanceClient);
            transactionRepository.addTransactionHistory(newBalanceClient, transaction);
            clientRepository.addInAudit(login, "credit: " + transactionAmount);
            return true;
        }
        return false;
    }

    /**
     * Вывод истории транзакций по логину
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
