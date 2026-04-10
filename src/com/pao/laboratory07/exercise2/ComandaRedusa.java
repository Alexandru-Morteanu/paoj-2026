package com.pao.laboratory07.exercise2;

import com.pao.laboratory07.exercise1.OrderState;

public class ComandaRedusa implements Comanda {
    private String nume;
    private double pretInitial;
    private int discount;
    private OrderState stare;

    public ComandaRedusa(String nume, double pretInitial, int discount) {
        this.nume = nume;
        this.pretInitial = pretInitial;
        this.discount = discount;
        this.stare = OrderState.PLACED;
    }

    @Override
    public double pretFinal() {
        return pretInitial - (pretInitial * discount / 100.0);
    }

    @Override
    public String descriere() {
        return String.format(java.util.Locale.US, "DISCOUNTED: %s, pret: %.2f lei (-%d%%) [%s]",
                nume, pretFinal(), discount, stare);
    }
}