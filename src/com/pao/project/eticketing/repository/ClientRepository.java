package com.pao.project.eticketing.repository;

import com.pao.project.eticketing.model.Client;
import com.pao.project.eticketing.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements Repository<Client, String> {

    @Override
    public void save(Client entity) {
        String sql = "INSERT INTO clienti (id, nume, email, telefon) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getId());
            statement.setString(2, entity.getNume());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getTelefon());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea clientului", e);
        }
    }

    @Override
    public Optional<Client> findById(String id) {
        String sql = "SELECT id, nume, email, telefon FROM clienti WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea clientului", e);
        }
        return Optional.empty();
    }

    public Optional<Client> findByEmail(String email) {
        String sql = "SELECT id, nume, email, telefon FROM clienti WHERE email = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea clientului dupa email", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        String sql = "SELECT id, nume, email, telefon FROM clienti";
        List<Client> clienti = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                clienti.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea clientilor", e);
        }
        return clienti;
    }

    @Override
    public void update(Client entity) {
        String sql = "UPDATE clienti SET nume = ?, email = ?, telefon = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getNume());
            statement.setString(2, entity.getEmail());
            statement.setString(3, entity.getTelefon());
            statement.setString(4, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea clientului", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM clienti WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea clientului", e);
        }
    }

    private Client mapRow(ResultSet resultSet) throws SQLException {
        return new Client(
                resultSet.getString("id"),
                resultSet.getString("nume"),
                resultSet.getString("email"),
                resultSet.getString("telefon")
        );
    }
}
