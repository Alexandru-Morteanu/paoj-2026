package com.pao.laboratory06.exercise2.colaboratori;

import com.pao.laboratory06.exercise2.PersoanaFizica;
import com.pao.laboratory06.exercise2.TipColaborator;

import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica {
    private boolean bonus;

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitBrutLunar = in.nextDouble();
        String b = in.next();
        bonus = b.equals("DA");
    }

    @Override
    public void afiseaza() {
        System.out.printf("CIM: %s %s, venit net anual: %.2f lei\n",
                nume, prenume, calculeazaVenitNetAnual());
    }

    @Override
    public String tipContract() {
        return "CIM";
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.CIM;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNetAnual = venitBrutLunar * 12 * 0.55;
        if (bonus) {
            venitNetAnual *= 1.10;
        }
        return venitNetAnual;
    }
}