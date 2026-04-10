package com.pao.laboratory07.exercise2;

import com.pao.laboratory07.exercise1.OrderState;

public class ComandaGratuita implements Comanda {
    private String nume;
    private OrderState stare;

    public ComandaGratuita(String nume) {
        this.nume = nume;
        this.stare = OrderState.PLACED;
    }

    @Override
    public double pretFinal() {
        return 0.0;
    }

    @Override
    public String descriere() {
        return String.format("GIFT: %s, gratuit [%s]", nume, stare);
    }
}