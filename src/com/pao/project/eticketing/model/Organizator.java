package com.pao.project.eticketing.model;

public class Organizator extends Utilizator {
    private String companie;

    public Organizator(String id, String nume, String email, String companie) {
        super(id, nume, email);
        this.companie = companie;
    }

    @Override
    public String getTipUtilizator() {
        return "ORGANIZATOR";
    }

    public String getCompanie() { return companie; }
}