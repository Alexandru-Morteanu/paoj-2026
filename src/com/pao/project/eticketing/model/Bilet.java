package com.pao.project.eticketing.model;
import java.util.UUID;

// Bifează cerința: Clasă imutabilă (atribute final, fără setteri)
public final class Bilet {
    private final String idBilet;
    private final String idEveniment;
    private final String tipAcces;
    private final double pret;

    public Bilet(String idEveniment, String tipAcces, double pret) {
        this.idBilet = UUID.randomUUID().toString(); // generare ID unic
        this.idEveniment = idEveniment;
        this.tipAcces = tipAcces;
        this.pret = pret;
    }

    public String getIdBilet() { return idBilet; }
    public String getIdEveniment() { return idEveniment; }
    public String getTipAcces() { return tipAcces; }
    public double getPret() { return pret; }

    @Override
    public String toString() {
        return "Bilet{" + "id='" + idBilet + '\'' + ", acces='" + tipAcces + '\'' + ", pret=" + pret + '}';
    }
}