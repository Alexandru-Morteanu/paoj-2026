package com.pao.project.eticketing;

import com.pao.project.eticketing.exception.BiletEpuizatException;
import com.pao.project.eticketing.exception.EvenimentInexistentException;
import com.pao.project.eticketing.model.Client;
import com.pao.project.eticketing.model.Eveniment;
import com.pao.project.eticketing.model.Locatie;
import com.pao.project.eticketing.model.Tranzactie;
import com.pao.project.eticketing.service.ClientService;
import com.pao.project.eticketing.service.EvenimentService;
import com.pao.project.eticketing.service.LocatieService;
import com.pao.project.eticketing.util.SchemaInitializer;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        ClientService clientService = ClientService.getInstance();
        EvenimentService evenimentService = EvenimentService.getInstance();
        LocatieService locatieService = LocatieService.getInstance();

        try {
            SchemaInitializer.init();

            System.out.println("\n[Actiunea 1] Adaugare locatii...");
            Locatie arenaNationala = new Locatie("L1", "Arena Nationala", "Bucuresti", 55000);
            Locatie salaPalatului = new Locatie("L2", "Sala Palatului", "Bucuresti", 4000);
            locatieService.adaugaLocatie(arenaNationala);
            locatieService.adaugaLocatie(salaPalatului);

            System.out.println("[Actiunea 2] Adaugare evenimente...");
            Eveniment ev1 = new Eveniment("E1", "Concert Coldplay", arenaNationala, LocalDateTime.of(2026, 6, 12, 20, 0));
            Eveniment ev2 = new Eveniment("E2", "Standup Comedy", salaPalatului, LocalDateTime.of(2026, 5, 20, 19, 30));
            Eveniment ev3 = new Eveniment("E3", "Festival Rock", arenaNationala, LocalDateTime.of(2026, 7, 15, 14, 0));

            evenimentService.adaugaEveniment(ev1);
            evenimentService.adaugaEveniment(ev2);
            evenimentService.adaugaEveniment(ev3);

            System.out.println("[Actiunea 3] Inregistrare clienti...");
            Client c1 = new Client("C1", "Ion Popescu", "ion@email.com", "0722000000");
            Client c2 = new Client("C2", "Ana Radu", "ana@email.com", "0733000000");
            clientService.inregistreazaClient(c1);
            clientService.inregistreazaClient(c2);

            System.out.println("\n[Actiunea 10] Listare sortata (ORDER BY data_timp)...");
            evenimentService.afiseazaEvenimenteCronologic();

            System.out.println("\n[Actiunea 7] Cautare evenimente dupa locatie...");
            evenimentService.afiseazaEvenimenteDinLocatie("Arena Nationala");

            System.out.println("\n[Actiunea 4] Emitere bilete...");
            Tranzactie t1 = evenimentService.achizitioneazaBilete("E1", c1.getEmail(), 2);
            System.out.println("Bilete cumparate cu succes de " + c1.getNume());

            System.out.println("\n[Actiunea 8] Verificare disponibilitate...");
            int locuriRamase = evenimentService.verificaDisponibilitate("E1");
            System.out.println("Locuri ramase la Concert Coldplay: " + locuriRamase);

            System.out.println("\n[Actiunea 9] Afisare istoric tranzactii client...");
            evenimentService.afiseazaIstoricBilete("ion@email.com");

            System.out.println("\n[Actiunea 5] Anulare tranzactie...");
            evenimentService.anuleazaTranzactie(t1);
            int locuriDupaAnulare = evenimentService.verificaDisponibilitate("E1");
            System.out.println("Locuri ramase dupa anulare: " + locuriDupaAnulare);

            System.out.println("\n[Actiunea 6] Cautare eveniment inexistent (Test Exceptie)...");
            try {
                evenimentService.gasesteEvenimentDupaId("E99");
            } catch (EvenimentInexistentException e) {
                System.out.println("Eroare prinsa corect: " + e.getMessage());
            }

            System.out.println("\n[Test Exceptie 2] Cumparare peste limita...");
            try {
                evenimentService.achizitioneazaBilete("E2", c2.getEmail(), 5000);
            } catch (BiletEpuizatException e) {
                System.out.println("Eroare prinsa corect: " + e.getMessage());
            }

            System.out.println("\nEtapa II: audit.csv generat, baza de date SQLite persistenta.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
