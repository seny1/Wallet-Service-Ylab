package org.elSasen.util;

import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String URL_KEY = "url";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    static {
        loadDriver();
    }

    private ConnectionManager() {
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Database getDatabase() {
        try {
            return DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(open()));
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
