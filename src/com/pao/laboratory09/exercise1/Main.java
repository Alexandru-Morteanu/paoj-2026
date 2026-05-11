package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data contSursa contDestinatie tip)
        // 2. Setează câmpul note = "procesat" pe fiecare tranzacție înainte de serializare
        // 3. Serializează lista de tranzacții în OUTPUT_FILE cu ObjectOutputStream (try-with-resources)
        // 4. Deserializează lista din OUTPUT_FILE cu ObjectInputStream (try-with-resources)
        // 5. Procesează comenzile din stdin până la EOF:
        //    - LIST          → afișează toate tranzacțiile, câte una pe linie
        //    - FILTER yyyy-MM → afișează tranzacțiile cu data care începe cu yyyy-MM
        //                       sau "Niciun rezultat." dacă nu există
        //    - NOTE id        → afișează "NOTE[id]: <valoarea câmpului note>"
        //                       sau "NOTE[id]: not found" dacă id-ul nu există
        //
        // Format linie tranzacție:
        //   [id] data tip: suma RON | contSursa -> contDestinatie
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON | RO01SRC1 -> RO01DST1

        Scanner scanner = new Scanner(System.in);
        List<Tranzactie> tranzactii = new ArrayList<>();

        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            String contSursa = scanner.next();
            String contDestinatie = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

            Tranzactie tranzactie = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            tranzactie.note = "procesat";
            tranzactii.add(tranzactie);
        }

        new File("output").mkdirs();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            out.writeObject(tranzactii);
        }

        List<Tranzactie> citite;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            citite = (List<Tranzactie>) in.readObject();
        }

        while (scanner.hasNext()) {
            String comanda = scanner.next();
            if (comanda.equals("LIST")) {
                for (Tranzactie tranzactie : citite) {
                    System.out.println(tranzactie);
                }
            } else if (comanda.equals("FILTER")) {
                String luna = scanner.next();
                boolean gasit = false;
                for (Tranzactie tranzactie : citite) {
                    if (tranzactie.data.startsWith(luna)) {
                        System.out.println(tranzactie);
                        gasit = true;
                    }
                }
                if (!gasit) {
                    System.out.println("Niciun rezultat.");
                }
            } else if (comanda.equals("NOTE")) {
                int id = scanner.nextInt();
                Tranzactie gasita = null;
                for (Tranzactie tranzactie : citite) {
                    if (tranzactie.id == id) {
                        gasita = tranzactie;
                        break;
                    }
                }
                if (gasita == null) {
                    System.out.println("NOTE[" + id + "]: not found");
                } else {
                    System.out.println("NOTE[" + id + "]: " + gasita.note);
                }
            }
        }
    }
}