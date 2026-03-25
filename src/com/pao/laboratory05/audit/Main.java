package com.pao.laboratory05.audit;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AngajatService service = AngajatService.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Sistem Angajați cu Audit =====");
            System.out.println("1. Adauga angajat");
            System.out.println("2. Listare dupa salariu");
            System.out.println("3. Cauta după departament");
            System.out.println("4. Afiseaza audit log");
            System.out.println("0. Iesire");
            System.out.print("Optiune: ");

            int optiune = -1;
            try {
                optiune = scanner.nextInt();
                scanner.nextLine(); 
            } catch (InputMismatchException e) {
                System.out.println("Eroare: Introdu un numar!");
                scanner.nextLine();
                continue;
            }

            switch (optiune) {
                case 1:
                    try {
                        System.out.print("Nume: ");
                        String nume = scanner.nextLine();
                        System.out.print("Departament: ");
                        String dName = scanner.nextLine();
                        System.out.print("Locatie: ");
                        String dLoc = scanner.nextLine();
                        System.out.print("Salariu: ");
                        double sal = scanner.nextDouble();
                        scanner.nextLine();

                        service.addAngajat(new Angajat(nume, new Departament(dName, dLoc), sal));
                    } catch (InputMismatchException e) {
                        System.out.println("Eroare la introducerea datelor numerice.");
                        scanner.nextLine();
                    }
                    break;
                case 2:
                    service.listBySalary();
                    break;
                case 3:
                    System.out.print("Departament cautat: ");
                    service.findByDepartament(scanner.nextLine());
                    break;
                case 4:
                    service.printAuditLog();
                    break;
                case 0:
                    System.out.println("La revedere!");
                    return;
                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }
}