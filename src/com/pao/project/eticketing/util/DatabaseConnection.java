package com.pao.project.eticketing.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {
    private static DatabaseConnection instance;
    private final String url;
    private final String user;
    private final String password;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Driver SQLite JDBC nu a fost gasit", e);
        }

        Properties properties = new Properties();
        try (InputStream input = DatabaseConnection.class
                .getResourceAsStream("/com/pao/project/eticketing/resources/db.properties")) {
            if (input == null) {
                throw new IllegalStateException("db.properties nu a fost gasit in classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Nu s-a putut citi db.properties", e);
        }

        url = properties.getProperty("db.url");
        user = properties.getProperty("db.user", "");
        password = properties.getProperty("db.password", "");
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            if (user == null || user.isBlank()) {
                connection = DriverManager.getConnection(url);
            } else {
                connection = DriverManager.getConnection(url, user, password);
            }
        }
        return connection;
    }
}
