package com.pao.project.eticketing.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Eveniment implements Comparable<Eveniment> {
    private String id;
    private String nume;
    private Locatie locatie;
    private LocalDateTime dataTimp;
    private int bileteVandute;

    public Eveniment(String id, String nume, Locatie locatie, LocalDateTime dataTimp) {
        this.id = id;
        this.nume = nume;
        this.locatie = locatie;
        this.dataTimp = dataTimp;
        this.bileteVandute = 0;
    }

    public String getId() { return id; }
    public String getNume() { return nume; }
    public Locatie getLocatie() { return locatie; }
    public LocalDateTime getDataTimp() { return dataTimp; }
    public int getBileteVandute() { return bileteVandute; }
    public int getLocuriDisponibile() { return locatie.getCapacitate() - bileteVandute; }

    public void setBileteVandute(int bileteVandute) { this.bileteVandute = bileteVandute; }
    public void vindeBilete(int numar) { this.bileteVandute += numar; }
    public void anuleazaBilete(int numar) { this.bileteVandute -= numar; }

    @Override
    public int compareTo(Eveniment altul) {
        return this.dataTimp.compareTo(altul.dataTimp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Eveniment that = (Eveniment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return nume + " la " + locatie.getNume() + " [" + dataTimp + "]";
    }
}