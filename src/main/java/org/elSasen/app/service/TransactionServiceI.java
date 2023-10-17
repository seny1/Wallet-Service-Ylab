package org.elSasen.app.service;

/**
 * Интерфейс сервиса, реализующий бизнес-логику транзакций
 */
public interface TransactionServiceI {

    /**
     * Снятие средство со счета
     * @param transactionAmount Сумма
     * @param login Логин клиента
     * @return Результат попытки снятия
     */
    boolean debit(int transactionAmount, String login);

    /**
     * Пополнение счета
     * @param transactionAmount Сумма
     * @param login Логин клиента
     */
    boolean credit(int transactionAmount, String login);

    /**
     * Вывод истории транзакций
     * @param login Логин клиента
     */
    void printHistory(String login);
}
