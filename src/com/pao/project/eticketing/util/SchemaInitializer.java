package com.pao.project.eticketing.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public final class SchemaInitializer {
    private SchemaInitializer() {
    }

    public static void init() throws SQLException, IOException {
        String schema;
        try (InputStream input = SchemaInitializer.class
                .getResourceAsStream("/com/pao/project/eticketing/schema.sql")) {
            if (input == null) {
                throw new IllegalStateException("schema.sql nu a fost gasit in classpath");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
                schema = reader.lines().collect(Collectors.joining("\n"));
            }
        }

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement statement = connection.createStatement()) {
            for (String sql : schema.split(";")) {
                String trimmed = sql.trim();
                if (!trimmed.isEmpty()) {
                    statement.execute(trimmed);
                }
            }
        }
    }
}
