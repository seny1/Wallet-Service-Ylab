package org.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс, описывающий сущность транзакции
 */
@Getter
@AllArgsConstructor
public class Transaction {

    /**
     * Поле, обозначающее уникальный номер транзакции
     */
    private final Integer id;
    /**
     * Поле, обозначающее тип транзакции, т.е. дебет или кредит
     */
    private final TypeOfTransaction type;
    /**
     * Поле, обозначающее сумму, которую клиент хочет снять или положить
     */
    private final Integer amount;
}
