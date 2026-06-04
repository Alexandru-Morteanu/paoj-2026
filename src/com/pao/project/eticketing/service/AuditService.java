package com.pao.project.eticketing.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

public final class AuditService {
    private static AuditService instance;
    private static final Path AUDIT_FILE = Path.of("audit.csv");
    private final ReentrantLock lock = new ReentrantLock();

    private AuditService() {
    }

    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void log(String numeActiune) {
        lock.lock();
        try {
            String line = numeActiune + "," + LocalDateTime.now() + System.lineSeparator();
            Files.writeString(
                    AUDIT_FILE,
                    line,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new RuntimeException("Eroare la scrierea in audit.csv", e);
        } finally {
            lock.unlock();
        }
    }
}
