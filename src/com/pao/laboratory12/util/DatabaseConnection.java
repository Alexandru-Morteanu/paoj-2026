package com.pao.laboratory12.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws IOException, SQLException {
        Properties props = new Properties();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) {
                throw new IOException("Nu gasesc db.properties in resources/");
            }

            props.load(is);
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        loadDriver(url);

        connection = DriverManager.getConnection(url, user, password);

        // pentru SQLite activam manual foreign key-urile
        if (url.contains("sqlite")) {
            try (var stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
        }
    }

    private void loadDriver(String url) throws SQLException {
        try {
            if (url.contains("sqlite")) {
                Class.forName("org.sqlite.JDBC");
            } else if (url.contains("h2")) {
                Class.forName("org.h2.Driver");
            } else if (url.contains("mysql")) {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Nu gasesc driverul JDBC pentru url-ul: " + url, e);
        }
    }

    public static synchronized DatabaseConnection getInstance() throws IOException, SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }

        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}