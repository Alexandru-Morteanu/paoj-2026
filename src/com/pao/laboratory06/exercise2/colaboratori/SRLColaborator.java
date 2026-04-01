package com.pao.laboratory06.exercise2.colaboratori;

import com.pao.laboratory06.exercise2.TipColaborator;

import java.util.Scanner;

public class SRLColaborator extends PersoanaJuridica {
    private double cheltuieli;

    @Override
    public void citeste(Scanner in) {
        String numeFirma = in.next();
        String tipFirma = in.next();
        nume = numeFirma + " " + tipFirma;
        venitBrutLunar = in.nextDouble();
        cheltuieli = in.nextDouble();
    }

    @Override
    public void afiseaza() {
        System.out.printf("SRL: %s, venit net anual: %.2f lei\n",
                nume, calculeazaVenitNetAnual());
    }

    @Override
    public String tipContract() {
        return "SRL";
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.SRL;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        return (venitBrutLunar - cheltuieli) * 12 * 0.84;
    }
}