package org.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Класс, описывающий сущность клиента
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Client {

    /**
     * Поле, обозначающее логин клиента
     */
    private final String login;
    /**
     * Поле, обозначающее пароль клиента
     */
    private final String password;
    /**
     * Поле, обозначающее баланс клиента
     */
    private final Integer balance;
}
