package org.app.service;

/**
 * Интерфейс сервиса, реализующий бизнес-логику транзакций
 */
public interface TransactionServiceI {

    /**
     * Снятие средство со счета
     * @param transactionId Уникальный номер транзакции
     * @param transactionAmount Сумма
     * @param login Логин клиента
     * @return Результат попытки снятия
     */
    boolean debit(int transactionId, int transactionAmount, String login);

    /**
     * Пополнение счета
     * @param transactionId Уникальный номер транзакции
     * @param transactionAmount Сумма
     * @param login Логин клиента
     * @return Результат попытки пополнения
     */
    boolean credit(int transactionId, int transactionAmount, String login);

    /**
     * Вывод истории транзакций
     * @param login Логин клиента
     */
    void printHistory(String login);
}
