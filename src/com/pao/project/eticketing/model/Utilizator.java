package com.pao.project.eticketing.model;

import java.util.Objects;

public abstract class Utilizator {
    protected String id;
    protected String nume;
    protected String email;

    public Utilizator(String id, String nume, String email) {
        this.id = id;
        this.nume = nume;
        this.email = email;
    }

    public abstract String getTipUtilizator();

    public String getId() { return id; }
    public String getNume() { return nume; }
    public String getEmail() { return email; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilizator that = (Utilizator) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}