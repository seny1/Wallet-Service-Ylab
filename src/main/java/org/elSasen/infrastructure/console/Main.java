package org.elSasen.infrastructure.console;

import org.elSasen.util.CreateDB;

public class Main {
    public static void main(String[] args) throws Exception {
        CreateDB.createDB();
        new AppLogic().work();
    }
}
