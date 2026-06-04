package com.pao.project.eticketing.service;

import com.pao.project.eticketing.model.Client;
import com.pao.project.eticketing.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

public class ClientService {
    private static ClientService instance;
    private final ClientRepository clientRepository;
    private final AuditService auditService;

    private ClientService() {
        this.clientRepository = new ClientRepository();
        this.auditService = AuditService.getInstance();
    }

    public static synchronized ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }

    public void inregistreazaClient(Client client) {
        if (client != null && client.getEmail() != null) {
            clientRepository.save(client);
            auditService.log("inregistreaza_client");
        }
    }

    public Optional<Client> gasesteClientDupaEmail(String email) {
        auditService.log("cauta_client_dupa_email");
        return clientRepository.findByEmail(email);
    }

    public void stergeClient(String id) {
        clientRepository.delete(id);
        auditService.log("sterge_client");
    }

    public List<Client> afiseazaTotiClientii() {
        auditService.log("listeaza_clienti");
        return clientRepository.findAll();
    }
}
