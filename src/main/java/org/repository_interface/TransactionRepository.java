package org.repository_interface;

import org.domain.Client;
import org.domain.Transaction;
import org.domain.TypeOfTransaction;

/**
 * Интерфейс, предоставляющий методы для работы с транзакциями
 */
public interface TransactionRepository {

    /**
     * Проверка валидности уникального номера транзакции
     * @param id Уникальный номер транзакции
     * @return Результат проверки
     */
    boolean validateId(int id);

    /**
     * Проверка валидности суммы транзакции
     * @param typeOfTransaction Тип транзакции
     * @param amount Сумма транзакции
     * @param client Клиент
     * @return Результат проверки
     */
    boolean validateAmount(TypeOfTransaction typeOfTransaction, int amount, Client client);

    /**
     * Добавление транзации в память
     * @param transaction Транзакция
     */
    void addTransaction(Transaction transaction);

    /**
     * Добавление транзакции для каждого клиента в его историю
     * @param client Клиент
     * @param transaction Транзакция
     */
    void addTransactionHistory(Client client, Transaction transaction);

    /**
     * Вывод истории транзакций для каждого клиента
     * @param client Клиент
     */
    void printHistory(Client client);
}
