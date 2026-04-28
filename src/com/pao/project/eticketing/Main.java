package com.pao.project.eticketing;

import com.pao.project.eticketing.exception.BiletEpuizatException;
import com.pao.project.eticketing.exception.EvenimentInexistentException;
import com.pao.project.eticketing.model.Client;
import com.pao.project.eticketing.model.Eveniment;
import com.pao.project.eticketing.model.Locatie;
import com.pao.project.eticketing.model.Tranzactie;
import com.pao.project.eticketing.service.ClientService;
import com.pao.project.eticketing.service.EvenimentService;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        ClientService clientService = ClientService.getInstance();
        EvenimentService evenimentService = EvenimentService.getInstance();

        try {
            System.out.println("\n[Actiunea 1] Adăugare locații...");
            Locatie arenaNationala = new Locatie("Arena Națională", "București", 55000);
            Locatie salaPalatului = new Locatie("Sala Palatului", "București", 4000);

            System.out.println("[Actiunea 2] Adăugare evenimente...");
            Eveniment ev1 = new Eveniment("E1", "Concert Coldplay", arenaNationala, LocalDateTime.of(2026, 6, 12, 20, 0));
            Eveniment ev2 = new Eveniment("E2", "Standup Comedy", salaPalatului, LocalDateTime.of(2026, 5, 20, 19, 30));
            Eveniment ev3 = new Eveniment("E3", "Festival Rock", arenaNationala, LocalDateTime.of(2026, 7, 15, 14, 0));

            evenimentService.adaugaEveniment(ev1);
            evenimentService.adaugaEveniment(ev2);
            evenimentService.adaugaEveniment(ev3);

            System.out.println("[Actiunea 3] Înregistrare clienți...");
            Client c1 = new Client("C1", "Ion Popescu", "ion@email.com", "0722000000");
            Client c2 = new Client("C2", "Ana Radu", "ana@email.com", "0733000000");
            clientService.inregistreazaClient(c1);
            clientService.inregistreazaClient(c2);

            System.out.println("\n[Actiunea 10] Listare sortată (prin TreeSet)...");
            evenimentService.afiseazaEvenimenteCronologic();

            System.out.println("\n[Actiunea 7] Căutare evenimente după locație...");
            evenimentService.afiseazaEvenimenteDinLocatie("Arena Națională");

            System.out.println("\n[Actiunea 4] Emitere bilete...");
            Tranzactie t1 = evenimentService.achizitioneazaBilete("E1", c1.getEmail(), 2);
            System.out.println("Bilete cumparate cu succes de " + c1.getNume());

            System.out.println("\n[Actiunea 8] Verificare disponibilitate...");
            System.out.println("Locuri rămase la Concert Coldplay: " + evenimentService.gasesteEvenimentDupaId("E1").getLocuriDisponibile());

            System.out.println("\n[Actiunea 9] Afișare istoric tranzacții client...");
            evenimentService.afiseazaIstoricBilete("ion@email.com");

            System.out.println("\n[Actiunea 5] Anulare tranzacție...");
            evenimentService.anuleazaTranzactie(t1);
            System.out.println("Locuri rămase după anulare: " + evenimentService.gasesteEvenimentDupaId("E1").getLocuriDisponibile());

            System.out.println("\n[Actiunea 6] Căutare eveniment inexistent (Test Excepție)...");
            try {
                evenimentService.gasesteEvenimentDupaId("E99");
            } catch (EvenimentInexistentException e) {
                System.out.println("Eroare prinsă corect: " + e.getMessage());
            }

            System.out.println("\n[Test Excepție 2] Cumpărare peste limită...");
            try {
                evenimentService.achizitioneazaBilete("E2", c2.getEmail(), 5000); // Sala Palatului are 4000
            } catch (BiletEpuizatException e) {
                System.out.println("Eroare prinsă corect: " + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}