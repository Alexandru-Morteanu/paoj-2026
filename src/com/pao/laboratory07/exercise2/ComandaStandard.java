package com.pao.laboratory07.exercise2;

import com.pao.laboratory07.exercise1.OrderState;

public class ComandaStandard implements Comanda {
    private String nume;
    private double pret;
    private OrderState stare;

    public ComandaStandard(String nume, double pret) {
        this.nume = nume;
        this.pret = pret;
        this.stare = OrderState.PLACED; // Toate comenzile noi pornesc din PLACED
    }

    @Override
    public double pretFinal() {
        return pret;
    }

    @Override
    public String descriere() {
        return String.format(java.util.Locale.US, "STANDARD: %s, pret: %.2f lei [%s]", nume, pret, stare);
    }
}