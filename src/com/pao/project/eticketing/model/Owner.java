package com.pao.project.eticketing.model;

public class Owner extends Organizator {
    private double cotaProfit; // Procentul din vânzări care revine proprietarului platformei

    public Owner(String id, String nume, String email, String companie, double cotaProfit) {
        super(id, nume, email, companie);
        this.cotaProfit = cotaProfit;
    }

    @Override
    public String getTipUtilizator() {
        return "OWNER_PLATFORMA";
    }

    public double getCotaProfit() {
        return cotaProfit;
    }

    public void setCotaProfit(double cotaProfit) {
        this.cotaProfit = cotaProfit;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "nume='" + nume + '\'' +
                ", companie='" + getCompanie() + '\'' +
                ", cotaProfit=" + cotaProfit +
                '}';
    }
}