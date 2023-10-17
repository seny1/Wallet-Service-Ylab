package org.elSasen.util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateDB {

    public static void createDB() {
        Database database = ConnectionManager.getDatabase();
        try (Connection connection = ConnectionManager.open()) {
            String sql = new String(Files.readAllBytes(Path.of("src/main/resources/db/changelog/create-schema.sql")));
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

            database.setDefaultSchemaName(PropertiesUtil.get("defaultSchemaName"));
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (SQLException | IOException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}
