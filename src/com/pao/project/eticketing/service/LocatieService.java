package com.pao.project.eticketing.service;

import com.pao.project.eticketing.model.Locatie;
import com.pao.project.eticketing.repository.LocatieRepository;

import java.util.List;
import java.util.Optional;

public class LocatieService {
    private static LocatieService instance;
    private final LocatieRepository locatieRepository;
    private final AuditService auditService;

    private LocatieService() {
        this.locatieRepository = new LocatieRepository();
        this.auditService = AuditService.getInstance();
    }

    public static synchronized LocatieService getInstance() {
        if (instance == null) {
            instance = new LocatieService();
        }
        return instance;
    }

    public void adaugaLocatie(Locatie locatie) {
        if (locatie == null) {
            return;
        }
        locatieRepository.save(locatie);
        auditService.log("adauga_locatie");
    }

    public Optional<Locatie> gasesteLocatieDupaId(String id) {
        auditService.log("cauta_locatie_dupa_id");
        return locatieRepository.findById(id);
    }

    public List<Locatie> listeazaToateLocatiile() {
        auditService.log("listeaza_locatii");
        return locatieRepository.findAll();
    }

    public void stergeLocatie(String id) {
        locatieRepository.delete(id);
        auditService.log("sterge_locatie");
    }
}
