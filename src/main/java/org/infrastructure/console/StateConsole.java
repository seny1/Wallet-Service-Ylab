package org.infrastructure.console;

/**
 * Интерфейс состояния приложения
 */
public interface StateConsole {

    /**
     * Метод для работы нужного состояния
     * @throws Exception Любая ошибка
     */
    void process() throws Exception;

    /**
     * Метод для перехода в следующее состояние
     * @return Следующее состояние
     */
    StateConsole nextState();
}
