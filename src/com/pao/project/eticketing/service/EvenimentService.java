package com.pao.project.eticketing.service;

import com.pao.project.eticketing.exception.BiletEpuizatException;
import com.pao.project.eticketing.exception.EvenimentInexistentException;
import com.pao.project.eticketing.model.Bilet;
import com.pao.project.eticketing.model.Eveniment;
import com.pao.project.eticketing.model.Tranzactie;
import com.pao.project.eticketing.repository.BiletRepository;
import com.pao.project.eticketing.repository.EvenimentRepository;
import com.pao.project.eticketing.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class EvenimentService {
    private static EvenimentService instance;
    private final EvenimentRepository evenimentRepository;
    private final AuditService auditService;

    private EvenimentService() {
        this.evenimentRepository = new EvenimentRepository();
        this.auditService = AuditService.getInstance();
    }

    public static synchronized EvenimentService getInstance() {
        if (instance == null) {
            instance = new EvenimentService();
        }
        return instance;
    }

    public void adaugaEveniment(Eveniment eveniment) {
        if (eveniment != null) {
            evenimentRepository.save(eveniment);
            auditService.log("adauga_eveniment");
        }
    }

    public Eveniment gasesteEvenimentDupaId(String id) throws EvenimentInexistentException {
        auditService.log("cauta_eveniment_dupa_id");
        return evenimentRepository.findById(id)
                .orElseThrow(() -> new EvenimentInexistentException("Evenimentul cu ID " + id + " nu a fost gasit!"));
    }

    public Tranzactie achizitioneazaBilete(String idEveniment, String emailClient, int numarBilete)
            throws EvenimentInexistentException, BiletEpuizatException {
        Eveniment eveniment = evenimentRepository.findById(idEveniment)
                .orElseThrow(() -> new EvenimentInexistentException("Evenimentul cu ID " + idEveniment + " nu a fost gasit!"));

        if (eveniment.getLocuriDisponibile() < numarBilete) {
            throw new BiletEpuizatException("Nu sunt suficiente locuri disponibile pentru " + eveniment.getNume());
        }

        Tranzactie tranzactie = new Tranzactie("TRZ-" + System.currentTimeMillis(), emailClient);
        for (int i = 0; i < numarBilete; i++) {
            tranzactie.adaugaBilet(BiletRepository.create(idEveniment, "Acces General", 150.0));
        }

        executaAchizitieInTranzactie(eveniment, tranzactie, numarBilete);
        auditService.log("achizitioneaza_bilete");
        return tranzactie;
    }

    private void executaAchizitieInTranzactie(Eveniment eveniment, Tranzactie tranzactie, int numarBilete)
            throws BiletEpuizatException {
        Connection connection;
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            String updateEveniment = "UPDATE evenimente SET bilete_vandute = bilete_vandute + ? WHERE id = ? AND bilete_vandute + ? <= (SELECT capacitate FROM locatii WHERE id = ?)";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateEveniment)) {
                updateStatement.setInt(1, numarBilete);
                updateStatement.setString(2, eveniment.getId());
                updateStatement.setInt(3, numarBilete);
                updateStatement.setString(4, eveniment.getLocatie().getId());
                int updated = updateStatement.executeUpdate();
                if (updated == 0) {
                    connection.rollback();
                    throw new BiletEpuizatException("Nu sunt suficiente locuri disponibile pentru " + eveniment.getNume());
                }
            }

            String insertTranzactie = "INSERT INTO tranzactii (id, id_client, data_tranzactie) VALUES (?, ?, ?)";
            try (PreparedStatement tranzactieStatement = connection.prepareStatement(insertTranzactie)) {
                tranzactieStatement.setString(1, tranzactie.getId());
                tranzactieStatement.setString(2, tranzactie.getIdClient());
                tranzactieStatement.setString(3, tranzactie.getDataTranzactie().toString());
                tranzactieStatement.executeUpdate();
            }

            String insertBilet = "INSERT INTO bilete (id_bilet, id_tranzactie, id_eveniment, tip_acces, pret) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement biletStatement = connection.prepareStatement(insertBilet)) {
                for (Bilet bilet : tranzactie.getBilete()) {
                    biletStatement.setString(1, bilet.getIdBilet());
                    biletStatement.setString(2, tranzactie.getId());
                    biletStatement.setString(3, bilet.getIdEveniment());
                    biletStatement.setString(4, bilet.getTipAcces());
                    biletStatement.setDouble(5, bilet.getPret());
                    biletStatement.addBatch();
                }
                biletStatement.executeBatch();
            }

            connection.commit();
            eveniment.vindeBilete(numarBilete);
        } catch (SQLException e) {
            try {
                DatabaseConnection.getInstance().getConnection().rollback();
            } catch (SQLException rollbackException) {
                rollbackException.addSuppressed(e);
                throw new RuntimeException("Eroare la rollback pentru achizitie", rollbackException);
            }
            throw new RuntimeException("Eroare la achizitionarea biletelor", e);
        } finally {
            try {
                Connection connectionFinally = DatabaseConnection.getInstance().getConnection();
                connectionFinally.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Eroare la resetarea autocommit", e);
            }
        }
    }

    public void afiseazaEvenimenteCronologic() {
        auditService.log("ordoneaza_cronologic");
        System.out.println("--- Calendar Evenimente ---");
        for (Eveniment eveniment : evenimentRepository.findAllOrderedByDate()) {
            System.out.println(eveniment + " | Locuri libere: " + eveniment.getLocuriDisponibile());
        }
    }

    public void afiseazaEvenimenteDinLocatie(String numeLocatie) {
        auditService.log("listeaza_evenimente_dupa_locatie");
        System.out.println("--- Evenimente in " + numeLocatie + " ---");
        for (Eveniment eveniment : evenimentRepository.findByLocatieNume(numeLocatie)) {
            System.out.println(eveniment);
        }
    }

    public int verificaDisponibilitate(String idEveniment) throws EvenimentInexistentException {
        auditService.log("verifica_disponibilitate");
        return evenimentRepository.findById(idEveniment)
                .orElseThrow(() -> new EvenimentInexistentException("Evenimentul cu ID " + idEveniment + " nu a fost gasit!"))
                .getLocuriDisponibile();
    }

    public void anuleazaTranzactie(Tranzactie tranzactie) throws EvenimentInexistentException {
        if (tranzactie == null || tranzactie.getBilete().isEmpty()) {
            return;
        }

        String idEveniment = tranzactie.getBilete().get(0).getIdEveniment();
        int numarBilete = tranzactie.getBilete().size();

        Connection connection;
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            String deleteBilete = "DELETE FROM bilete WHERE id_tranzactie = ?";
            try (PreparedStatement deleteBileteStatement = connection.prepareStatement(deleteBilete)) {
                deleteBileteStatement.setString(1, tranzactie.getId());
                deleteBileteStatement.executeUpdate();
            }

            String deleteTranzactie = "DELETE FROM tranzactii WHERE id = ?";
            try (PreparedStatement deleteTranzactieStatement = connection.prepareStatement(deleteTranzactie)) {
                deleteTranzactieStatement.setString(1, tranzactie.getId());
                deleteTranzactieStatement.executeUpdate();
            }

            String updateEveniment = "UPDATE evenimente SET bilete_vandute = bilete_vandute - ? WHERE id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateEveniment)) {
                updateStatement.setInt(1, numarBilete);
                updateStatement.setString(2, idEveniment);
                updateStatement.executeUpdate();
            }

            connection.commit();
            auditService.log("anuleaza_tranzactie");
        } catch (SQLException e) {
            try {
                DatabaseConnection.getInstance().getConnection().rollback();
            } catch (SQLException rollbackException) {
                rollbackException.addSuppressed(e);
                throw new RuntimeException("Eroare la rollback pentru anulare", rollbackException);
            }
            throw new RuntimeException("Eroare la anularea tranzactiei", e);
        } finally {
            try {
                DatabaseConnection.getInstance().getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Eroare la resetarea autocommit", e);
            }
        }
    }

    public void afiseazaIstoricBilete(String emailClient) {
        auditService.log("afiseaza_istoric_client");
        System.out.println("--- Istoric Bilete pentru " + emailClient + " ---");
        List<String> istoric = evenimentRepository.findIstoricClient(emailClient);
        if (istoric.isEmpty()) {
            System.out.println("Nu exista tranzactii pentru acest client.");
            return;
        }
        for (String linie : istoric) {
            System.out.println(linie);
        }
    }
}
