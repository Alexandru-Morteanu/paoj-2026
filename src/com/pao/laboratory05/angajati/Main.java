package com.pao.laboratory05.angajati;

import java.util.Scanner;

/**
 * Exercise 3 — Angajați
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 3 — Angajați"
 *
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {
        AngajatService service = AngajatService.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu (descrescător)");
            System.out.println("3. Caută după departament");
            System.out.println("4. Afișează toți angajații (nesortat)");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            switch (optiune) {
                case 1:
                    System.out.print("Nume angajat: ");
                    String nume = scanner.nextLine();
                    System.out.print("Nume departament: ");
                    String numeDept = scanner.nextLine();
                    System.out.print("Locatie departament: ");
                    String locatieDept = scanner.nextLine();
                    System.out.print("Salariu: ");
                    double salariu = scanner.nextDouble();
                    scanner.nextLine();

                    Departament d = new Departament(numeDept, locatieDept);
                    service.addAngajat(new Angajat(nume, d, salariu));
                    break;

                case 2:
                    System.out.println("--- Angajati sortati dupa salariu ---");
                    service.listBySalary();
                    break;

                case 3:
                    System.out.print("Introduceți numele departamentului: ");
                    String cautaDept = scanner.nextLine();
                    service.findByDepartament(cautaDept);
                    break;

                case 4:
                    System.out.println("--- Toti angajatii ---");
                    service.printAll();
                    break;

                case 0:
                    System.out.println("Iesire din program...");
                    return;

                default:
                    System.out.println("Optiune invalida!");
            }
        }
    }
}