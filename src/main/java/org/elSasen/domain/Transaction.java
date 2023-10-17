package org.elSasen.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Класс, описывающий сущность транзакции
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Transaction {

    /**
     * Поле, обозначающее уникальный номер транзакции
     */
    private final Long id;
    /**
     * Поле, обозначающее тип транзакции, т.е. дебет или кредит
     */
    private final TypeOfTransaction type;
    /**
     * Поле, обозначающее сумму, которую клиент хочет снять или положить
     */
    private final Integer amount;
}
