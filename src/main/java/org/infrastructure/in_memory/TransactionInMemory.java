package org.infrastructure.in_memory;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.domain.Client;
import org.domain.Transaction;
import org.domain.TypeOfTransaction;
import org.repository_interface.TransactionRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для работы транзакций с памятью приложения
 */
public class TransactionInMemory implements TransactionRepository {

    /**
     * Словарь для всех транзакций с ключом - уникальным номером транзакции и значением - самой транзакцией
     */
    private final Map<Integer, Transaction> transactionMap = new HashMap<>();
    /**
     * Словарь для истории транзакций каждого клиента с ключом - логином и значением - транзакцией
     */
    private final MultiValuedMap<String, Transaction> history = new ArrayListValuedHashMap<>();

    /**
     * Поле для реализации паттерна проектирования Singleton
     */
    private final static TransactionRepository INSTANCE = new TransactionInMemory();

    /**
     * Проверка на валидность уникального номера транзакции
     * @param transactionId Уникальный номер транзакции
     * @return Результат проверки
     */
    @Override
    public boolean validateId(int transactionId) {
        return !transactionMap.containsKey(transactionId);
    }

    /**
     * Проверка на валидность суммы транзакции
     * @param typeOfTransaction Тип транзакции
     * @param amount Сумма транзакции
     * @param client Клиент
     * @return Резульат проверки
     */
    @Override
    public boolean validateAmount(TypeOfTransaction typeOfTransaction, int amount, Client client) {
        if (typeOfTransaction == TypeOfTransaction.Credit) {
            return true;
        }
        return amount < client.getBalance();
    }

    /**
     * Добавление транзакции в словарь
     * @param transaction Транзакция
     */
    @Override
    public void addTransaction(Transaction transaction) {
        transactionMap.put(transaction.getId(), transaction);
    }

    /**
     * Добавление транзакции в историю транзакций клиента
     * @param client Клиент
     * @param transaction Транзакция
     */
    @Override
    public void addTransactionHistory(Client client, Transaction transaction) {
        history.put(client.getLogin(), transaction);
    }

    /**
     * Вывод истории транзакций определенного клиента
     * @param client Клиент
     */
    @Override
    public void printHistory(Client client) {
        history.get(client.getLogin()).forEach(transaction -> {
            System.out.print(transaction.getId() + " " + transaction.getType() + " " + transaction.getAmount() + "\n");
        });
    }

    /**
     * Метод для реализации паттерна проектирования Singleton
     * @return Экземпляр класса
     */
    public static TransactionRepository getInstance() {
        return INSTANCE;
    }
}
