package com.pao.project.eticketing.repository;

import com.pao.project.eticketing.model.Tranzactie;
import com.pao.project.eticketing.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TranzactieRepository implements Repository<Tranzactie, String> {

    @Override
    public void save(Tranzactie entity) {
        String sql = "INSERT INTO tranzactii (id, id_client, data_tranzactie) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getId());
            statement.setString(2, entity.getIdClient());
            statement.setString(3, entity.getDataTranzactie().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea tranzactiei", e);
        }
    }

    @Override
    public Optional<Tranzactie> findById(String id) {
        String sql = "SELECT id, id_client, data_tranzactie FROM tranzactii WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea tranzactiei", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Tranzactie> findAll() {
        String sql = "SELECT id, id_client, data_tranzactie FROM tranzactii";
        List<Tranzactie> tranzactii = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                tranzactii.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea tranzactiilor", e);
        }
        return tranzactii;
    }

    @Override
    public void update(Tranzactie entity) {
        String sql = "UPDATE tranzactii SET id_client = ?, data_tranzactie = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getIdClient());
            statement.setString(2, entity.getDataTranzactie().toString());
            statement.setString(3, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea tranzactiei", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM tranzactii WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea tranzactiei", e);
        }
    }

    private Tranzactie mapRow(ResultSet resultSet) throws SQLException {
        return new Tranzactie(
                resultSet.getString("id"),
                resultSet.getString("id_client")
        );
    }
}
