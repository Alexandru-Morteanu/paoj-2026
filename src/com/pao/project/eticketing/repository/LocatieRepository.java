package com.pao.project.eticketing.repository;

import com.pao.project.eticketing.model.Locatie;
import com.pao.project.eticketing.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocatieRepository implements Repository<Locatie, String> {

    @Override
    public void save(Locatie entity) {
        String sql = "INSERT INTO locatii (id, nume, adresa, capacitate) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getId());
            statement.setString(2, entity.getNume());
            statement.setString(3, entity.getAdresa());
            statement.setInt(4, entity.getCapacitate());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea locatiei", e);
        }
    }

    @Override
    public Optional<Locatie> findById(String id) {
        String sql = "SELECT id, nume, adresa, capacitate FROM locatii WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea locatiei", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Locatie> findAll() {
        String sql = "SELECT id, nume, adresa, capacitate FROM locatii";
        List<Locatie> locatii = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                locatii.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea locatiilor", e);
        }
        return locatii;
    }

    @Override
    public void update(Locatie entity) {
        String sql = "UPDATE locatii SET nume = ?, adresa = ?, capacitate = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getNume());
            statement.setString(2, entity.getAdresa());
            statement.setInt(3, entity.getCapacitate());
            statement.setString(4, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea locatiei", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM locatii WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea locatiei", e);
        }
    }

    private Locatie mapRow(ResultSet resultSet) throws SQLException {
        return new Locatie(
                resultSet.getString("id"),
                resultSet.getString("nume"),
                resultSet.getString("adresa"),
                resultSet.getInt("capacitate")
        );
    }
}
