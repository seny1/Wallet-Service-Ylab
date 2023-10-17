package org.elSasen.repository_interface;

import org.elSasen.domain.Client;
import org.elSasen.domain.Transaction;
import org.elSasen.domain.TypeOfTransaction;

/**
 * Интерфейс, предоставляющий методы для работы с транзакциями
 */
public interface TransactionRepository {

    /**
     * Проверка валидности суммы транзакции
     * @param typeOfTransaction Тип транзакции
     * @param amount Сумма транзакции
     * @param client Клиент
     * @return Результат проверки
     */
    boolean validateAmount(TypeOfTransaction typeOfTransaction, int amount, Client client);

    /**
     * Добавление транзакции в память
     * @param type Тип транзакции
     * @param amount Сумма
     */
    Long addTransaction(TypeOfTransaction type, int amount);

    /**
     * Добавление транзакции для каждого клиента в его историю
     * @param client Клиент
     * @param typeOfTransaction Тип транзакции
     * @param amount Сумма
     */
    int addTransactionHistory(Client client, TypeOfTransaction typeOfTransaction, int amount);

    /**
     * Вывод истории транзакций для каждого клиента
     * @param client Клиент
     */
    void printHistory(Client client);
}
