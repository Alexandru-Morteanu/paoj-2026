package com.pao.project.eticketing.service;

import com.pao.project.eticketing.exception.BiletEpuizatException;
import com.pao.project.eticketing.exception.EvenimentInexistentException;
import com.pao.project.eticketing.model.Bilet;
import com.pao.project.eticketing.model.Eveniment;
import com.pao.project.eticketing.model.Tranzactie;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class EvenimentService {
    private static EvenimentService instance;
    private final Set<Eveniment> evenimente;
    private final List<Tranzactie> tranzactii;

    private EvenimentService() {
        this.evenimente = new TreeSet<>();
        this.tranzactii = new ArrayList<>();
    }

    public static EvenimentService getInstance() {
        if (instance == null) {
            instance = new EvenimentService();
        }
        return instance;
    }

    public void adaugaEveniment(Eveniment e) {
        if (e != null) {
            evenimente.add(e);
        }
    }

    public Eveniment gasesteEvenimentDupaId(String id) throws EvenimentInexistentException {
        for (Eveniment e : evenimente) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        throw new EvenimentInexistentException("Evenimentul cu ID " + id + " nu a fost gasit!");
    }

    public Tranzactie achizitioneazaBilete(String idEveniment, String emailClient, String numeCategorie, int numarBilete)
            throws EvenimentInexistentException, BiletEpuizatException {

        Eveniment e = gasesteEvenimentDupaId(idEveniment);

        e.vindeBilete(numarBilete);

        Tranzactie t = new Tranzactie("TRZ-" + System.currentTimeMillis(), emailClient);

        return t;
    }

    public void afiseazaEvenimenteCronologic() {
        System.out.println("--- Calendar Evenimente ---");
        for (Eveniment e : evenimente) {
            System.out.println(e + " | Locuri libere: " + e.getLocuriDisponibile());
        }
    }

    public void afiseazaEvenimenteDinLocatie(String numeLocatie) {
        System.out.println("--- Evenimente in " + numeLocatie + " ---");
        for (Eveniment e : evenimente) {
            if (e.getLocatie().getNume().equalsIgnoreCase(numeLocatie)) {
                System.out.println(e);
            }
        }
    }

    public Tranzactie achizitioneazaBilete(String idEveniment, String emailClient, int numarBilete)
            throws EvenimentInexistentException, BiletEpuizatException {

        Eveniment e = gasesteEvenimentDupaId(idEveniment);

        if (e.getLocuriDisponibile() < numarBilete) {
            throw new BiletEpuizatException("Nu sunt suficiente locuri disponibile pentru " + e.getNume());
        }

        e.vindeBilete(numarBilete);

        Tranzactie t = new Tranzactie("TRZ-" + System.currentTimeMillis(), emailClient);
        for (int i = 0; i < numarBilete; i++) {
            t.adaugaBilet(new Bilet(e.getId(), "Acces General", 150.0));
        }
        tranzactii.add(t);
        return t;
    }

    public void anuleazaTranzactie(Tranzactie t) throws EvenimentInexistentException {
        if (t != null && !t.getBilete().isEmpty()) {
            Eveniment e = gasesteEvenimentDupaId(t.getBilete().get(0).getIdEveniment());
            e.anuleazaBilete(t.getBilete().size());
            tranzactii.remove(t);
        }
    }

    public void afiseazaIstoricBilete(String emailClient) {
        System.out.println("--- Istoric Bilete pentru " + emailClient + " ---");
        for (Tranzactie t : tranzactii) {
            if (t.getIdClient().equals(emailClient)) {
                System.out.println(t);
            }
        }
    }
}
