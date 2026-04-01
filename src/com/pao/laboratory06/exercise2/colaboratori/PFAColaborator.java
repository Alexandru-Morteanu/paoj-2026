package com.pao.laboratory06.exercise2.colaboratori;
import com.pao.laboratory06.exercise2.PersoanaFizica;
import com.pao.laboratory06.exercise2.TipColaborator;

import java.util.Scanner;

public class PFAColaborator extends PersoanaFizica {
    private double cheltuieli;

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitBrutLunar = in.nextDouble();
        cheltuieli = in.nextDouble();
    }

    @Override
    public void afiseaza() {
        System.out.printf("PFA: %s %s, venit net anual: %.2f lei\n",
                nume, prenume, calculeazaVenitNetAnual());
    }

    @Override
    public String tipContract() {
        return "PFA";
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.PFA;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = (venitBrutLunar - cheltuieli) * 12;
        double salariuMinim = 48600;

        double impozit = 0.10 * venitNet;

        double cass = 0;
        if (venitNet < 6 * salariuMinim) {
            cass = 0.10 * (6 * salariuMinim);
        } else if (venitNet <= 72 * salariuMinim) {
            cass = 0.10 * venitNet;
        } else {
            cass = 0.10 * (72 * salariuMinim);
        }

        double cas = 0;
        if (venitNet > 24 * salariuMinim) {
            cas = 0.25 * (24 * salariuMinim);
        } else if (venitNet >= 12 * salariuMinim) {
            cas = 0.25 * (12 * salariuMinim);
        }

        return venitNet - impozit - cass - cas;
    }
}