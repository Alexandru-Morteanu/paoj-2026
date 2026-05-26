package com.pao.laboratory12.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaInitializer {

    public static void init(Connection conn) throws SQLException, IOException {
        String url = conn.getMetaData().getURL();

        if (url.contains("sqlite")) {
            initSQLite(conn);
            System.out.println("[DB] Schema initializata pentru SQLite.");
        } else if (url.contains("h2")) {
            initH2(conn);
            System.out.println("[DB] Schema initializata pentru H2.");
        } else {
            throw new SQLException("Baza de date nesuportata pentru acest demo: " + url);
        }
    }

    private static void initSQLite(Connection conn) throws SQLException {
        String[] statements = {
                "DROP TABLE IF EXISTS loan",
                "DROP TABLE IF EXISTS book",
                "DROP TABLE IF EXISTS reader",
                "DROP TABLE IF EXISTS author",

                "CREATE TABLE author (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name VARCHAR(200) NOT NULL, " +
                        "country VARCHAR(100)" +
                        ")",

                "CREATE TABLE book (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title VARCHAR(300) NOT NULL, " +
                        "author_id INTEGER NOT NULL, " +
                        "available INTEGER NOT NULL DEFAULT 1, " +
                        "FOREIGN KEY (author_id) REFERENCES author(id)" +
                        ")",

                "CREATE TABLE reader (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name VARCHAR(200) NOT NULL, " +
                        "email VARCHAR(200)" +
                        ")",

                "CREATE TABLE loan (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "book_id INTEGER NOT NULL, " +
                        "reader_id INTEGER NOT NULL, " +
                        "loan_date VARCHAR(20) NOT NULL, " +
                        "return_date VARCHAR(20), " +
                        "FOREIGN KEY (book_id) REFERENCES book(id), " +
                        "FOREIGN KEY (reader_id) REFERENCES reader(id)" +
                        ")"
        };

        executeStatements(conn, statements);
    }

    private static void initH2(Connection conn) throws SQLException {
        String[] statements = {
                "DROP TABLE IF EXISTS loan",
                "DROP TABLE IF EXISTS book",
                "DROP TABLE IF EXISTS reader",
                "DROP TABLE IF EXISTS author",

                "CREATE TABLE author (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(200) NOT NULL, " +
                        "country VARCHAR(100)" +
                        ")",

                "CREATE TABLE book (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "title VARCHAR(300) NOT NULL, " +
                        "author_id BIGINT NOT NULL, " +
                        "available INTEGER NOT NULL DEFAULT 1, " +
                        "FOREIGN KEY (author_id) REFERENCES author(id)" +
                        ")",

                "CREATE TABLE reader (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(200) NOT NULL, " +
                        "email VARCHAR(200)" +
                        ")",

                "CREATE TABLE loan (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "book_id BIGINT NOT NULL, " +
                        "reader_id BIGINT NOT NULL, " +
                        "loan_date VARCHAR(20) NOT NULL, " +
                        "return_date VARCHAR(20), " +
                        "FOREIGN KEY (book_id) REFERENCES book(id), " +
                        "FOREIGN KEY (reader_id) REFERENCES reader(id)" +
                        ")"
        };

        executeStatements(conn, statements);
    }

    private static void executeStatements(Connection conn, String[] statements) throws SQLException {
        try (Statement statement = conn.createStatement()) {
            for (String sql : statements) {
                statement.execute(sql);
            }
        }
    }
}