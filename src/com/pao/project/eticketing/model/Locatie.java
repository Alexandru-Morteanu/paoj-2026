package com.pao.project.eticketing.model;

public class Locatie {
    private String id;
    private String nume;
    private String adresa;
    private int capacitate;

    public Locatie(String id, String nume, String adresa, int capacitate) {
        this.id = id;
        this.nume = nume;
        this.adresa = adresa;
        this.capacitate = capacitate;
    }

    public String getId() { return id; }
    public String getNume() { return nume; }
    public String getAdresa() { return adresa; }
    public int getCapacitate() { return capacitate; }

    @Override
    public String toString() { return nume + " (" + adresa + ")"; }
}
