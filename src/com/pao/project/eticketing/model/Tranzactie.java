package com.pao.project.eticketing.model;

// src/com/pao/proiect/eticketing/model/Tranzactie.java
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Tranzactie {
    private String id;
    private String idClient;
    private List<Bilet> bilete; // O colecție internă (List)
    private LocalDateTime dataTranzactie;

    public Tranzactie(String id, String idClient) {
        this.id = id;
        this.idClient = idClient;
        this.bilete = new ArrayList<>();
        this.dataTranzactie = LocalDateTime.now();
    }

    public void adaugaBilet(Bilet b) { this.bilete.add(b); }
    public List<Bilet> getBilete() { return bilete; }
    public String getIdClient() { return idClient; }

    @Override
    public String toString() {
        return "Tranzactie id=" + id + ", client=" + idClient + ", total bilete=" + bilete.size();
    }
}