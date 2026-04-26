package com.pao.project.eticketing.model;

public class Client extends Utilizator {
    private String telefon;

    public Client(String id, String nume, String email, String telefon) {
        super(id, nume, email);
        this.telefon = telefon;
    }

    @Override
    public String getTipUtilizator() {
        return "CLIENT";
    }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    @Override
    public String toString() {
        return "Client{" + "nume='" + nume + '\'' + ", email='" + email + '\'' + '}';
    }
}