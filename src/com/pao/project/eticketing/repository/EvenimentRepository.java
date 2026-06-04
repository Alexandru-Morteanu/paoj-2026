package com.pao.project.eticketing.repository;

import com.pao.project.eticketing.model.Eveniment;
import com.pao.project.eticketing.model.Locatie;
import com.pao.project.eticketing.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EvenimentRepository implements Repository<Eveniment, String> {

    @Override
    public void save(Eveniment entity) {
        String sql = "INSERT INTO evenimente (id, nume, locatie_id, data_timp, bilete_vandute) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getId());
            statement.setString(2, entity.getNume());
            statement.setString(3, entity.getLocatie().getId());
            statement.setString(4, entity.getDataTimp().toString());
            statement.setInt(5, entity.getBileteVandute());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea evenimentului", e);
        }
    }

    @Override
    public Optional<Eveniment> findById(String id) {
        String sql = """
                SELECT e.id, e.nume, e.data_timp, e.bilete_vandute,
                       l.id AS loc_id, l.nume AS loc_nume, l.adresa, l.capacitate
                FROM evenimente e
                JOIN locatii l ON e.locatie_id = l.id
                WHERE e.id = ?
                """;
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowWithLocatie(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea evenimentului", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Eveniment> findAll() {
        return findAllOrderedByDate();
    }

    public List<Eveniment> findAllOrderedByDate() {
        String sql = """
                SELECT e.id, e.nume, e.data_timp, e.bilete_vandute,
                       l.id AS loc_id, l.nume AS loc_nume, l.adresa, l.capacitate
                FROM evenimente e
                JOIN locatii l ON e.locatie_id = l.id
                ORDER BY e.data_timp ASC
                """;
        return executeJoinQuery(sql);
    }

    public List<Eveniment> findByLocatieNume(String numeLocatie) {
        String sql = """
                SELECT e.id, e.nume, e.data_timp, e.bilete_vandute,
                       l.id AS loc_id, l.nume AS loc_nume, l.adresa, l.capacitate
                FROM evenimente e
                JOIN locatii l ON e.locatie_id = l.id
                WHERE LOWER(l.nume) = LOWER(?)
                ORDER BY e.data_timp ASC
                """;
        List<Eveniment> evenimente = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, numeLocatie);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    evenimente.add(mapRowWithLocatie(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea evenimentelor dupa locatie", e);
        }
        return evenimente;
    }

    public List<String> findIstoricClient(String emailClient) {
        String sql = """
                SELECT t.id AS tranzactie_id, t.data_tranzactie,
                       b.id_bilet, b.tip_acces, b.pret,
                       e.nume AS eveniment_nume, l.nume AS locatie_nume
                FROM tranzactii t
                JOIN bilete b ON t.id = b.id_tranzactie
                JOIN evenimente e ON b.id_eveniment = e.id
                JOIN locatii l ON e.locatie_id = l.id
                WHERE t.id_client = ?
                ORDER BY t.data_tranzactie DESC
                """;
        List<String> linii = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, emailClient);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    linii.add(String.format(
                            "Tranzactie %s | %s @ %s | Bilet %s (%s) - %.2f RON",
                            resultSet.getString("tranzactie_id"),
                            resultSet.getString("eveniment_nume"),
                            resultSet.getString("locatie_nume"),
                            resultSet.getString("id_bilet"),
                            resultSet.getString("tip_acces"),
                            resultSet.getDouble("pret")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea istoricului clientului", e);
        }
        return linii;
    }

    @Override
    public void update(Eveniment entity) {
        String sql = "UPDATE evenimente SET nume = ?, locatie_id = ?, data_timp = ?, bilete_vandute = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getNume());
            statement.setString(2, entity.getLocatie().getId());
            statement.setString(3, entity.getDataTimp().toString());
            statement.setInt(4, entity.getBileteVandute());
            statement.setString(5, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea evenimentului", e);
        }
    }

    public void updateBileteVandute(String idEveniment, int bileteVandute) {
        String sql = "UPDATE evenimente SET bilete_vandute = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bileteVandute);
            statement.setString(2, idEveniment);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea biletelor vandute", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM evenimente WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea evenimentului", e);
        }
    }

    private List<Eveniment> executeJoinQuery(String sql) {
        List<Eveniment> evenimente = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                evenimente.add(mapRowWithLocatie(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea evenimentelor", e);
        }
        return evenimente;
    }

    private Eveniment mapRowWithLocatie(ResultSet resultSet) throws SQLException {
        Locatie locatie = new Locatie(
                resultSet.getString("loc_id"),
                resultSet.getString("loc_nume"),
                resultSet.getString("adresa"),
                resultSet.getInt("capacitate")
        );
        Eveniment eveniment = new Eveniment(
                resultSet.getString("id"),
                resultSet.getString("nume"),
                locatie,
                LocalDateTime.parse(resultSet.getString("data_timp"))
        );
        eveniment.setBileteVandute(resultSet.getInt("bilete_vandute"));
        return eveniment;
    }
}
