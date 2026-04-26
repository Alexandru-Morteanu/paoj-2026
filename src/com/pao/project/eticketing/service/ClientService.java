package com.pao.project.eticketing.service;

import com.pao.project.eticketing.model.Client;
import java.util.HashMap;
import java.util.Map;

// Bifează cerința: Serviciu Singleton și Map pentru stocare/indexare
public class ClientService {
    private static ClientService instance;
    private final Map<String, Client> clientiMap; // Indexare după Email

    private ClientService() {
        this.clientiMap = new HashMap<>();
    }

    public static ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }

    public void inregistreazaClient(Client client) {
        if (client != null && client.getEmail() != null) {
            clientiMap.put(client.getEmail(), client);
        }
    }

    public Client gasesteClientDupaEmail(String email) {
        return clientiMap.get(email);
    }

    public void stergeClient(String email) {
        clientiMap.remove(email);
    }

    public void afiseazaTotiClientii() {
        System.out.println("--- Lista Clienți ---");
        for (Client c : clientiMap.values()) {
            System.out.println(c);
        }
    }
}