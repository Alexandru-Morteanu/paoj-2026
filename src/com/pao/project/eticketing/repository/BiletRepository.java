package com.pao.project.eticketing.repository;

import com.pao.project.eticketing.model.Bilet;
import com.pao.project.eticketing.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BiletRepository implements Repository<Bilet, String> {

    public void save(Bilet entity, String idTranzactie) {
        String sql = "INSERT INTO bilete (id_bilet, id_tranzactie, id_eveniment, tip_acces, pret) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getIdBilet());
            statement.setString(2, idTranzactie);
            statement.setString(3, entity.getIdEveniment());
            statement.setString(4, entity.getTipAcces());
            statement.setDouble(5, entity.getPret());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea biletului", e);
        }
    }

    @Override
    public void save(Bilet entity) {
        throw new UnsupportedOperationException("Foloseste save(Bilet, idTranzactie)");
    }

    @Override
    public Optional<Bilet> findById(String id) {
        String sql = "SELECT id_bilet, id_eveniment, tip_acces, pret FROM bilete WHERE id_bilet = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea biletului", e);
        }
        return Optional.empty();
    }

    public List<Bilet> findByTranzactieId(String idTranzactie) {
        String sql = "SELECT id_bilet, id_eveniment, tip_acces, pret FROM bilete WHERE id_tranzactie = ?";
        List<Bilet> bilete = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, idTranzactie);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bilete.add(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea biletelor tranzactiei", e);
        }
        return bilete;
    }

    @Override
    public List<Bilet> findAll() {
        String sql = "SELECT id_bilet, id_eveniment, tip_acces, pret FROM bilete";
        List<Bilet> bilete = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                bilete.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea biletelor", e);
        }
        return bilete;
    }

    @Override
    public void update(Bilet entity) {
        String sql = "UPDATE bilete SET id_eveniment = ?, tip_acces = ?, pret = ? WHERE id_bilet = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getIdEveniment());
            statement.setString(2, entity.getTipAcces());
            statement.setDouble(3, entity.getPret());
            statement.setString(4, entity.getIdBilet());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea biletului", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM bilete WHERE id_bilet = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea biletului", e);
        }
    }

    public void deleteByTranzactieId(String idTranzactie) {
        String sql = "DELETE FROM bilete WHERE id_tranzactie = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, idTranzactie);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea biletelor tranzactiei", e);
        }
    }

    private Bilet mapRow(ResultSet resultSet) throws SQLException {
        return new Bilet(
                resultSet.getString("id_bilet"),
                resultSet.getString("id_eveniment"),
                resultSet.getString("tip_acces"),
                resultSet.getDouble("pret")
        );
    }

    public static Bilet create(String idEveniment, String tipAcces, double pret) {
        return new Bilet(UUID.randomUUID().toString(), idEveniment, tipAcces, pret);
    }
}
