DROP TABLE IF EXISTS bilete;
DROP TABLE IF EXISTS tranzactii;
DROP TABLE IF EXISTS evenimente;
DROP TABLE IF EXISTS clienti;
DROP TABLE IF EXISTS locatii;

CREATE TABLE locatii (
    id TEXT PRIMARY KEY,
    nume TEXT NOT NULL,
    adresa TEXT NOT NULL,
    capacitate INTEGER NOT NULL
);

CREATE TABLE evenimente (
    id TEXT PRIMARY KEY,
    nume TEXT NOT NULL,
    locatie_id TEXT NOT NULL,
    data_timp TEXT NOT NULL,
    bilete_vandute INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (locatie_id) REFERENCES locatii(id)
);

CREATE TABLE clienti (
    id TEXT PRIMARY KEY,
    nume TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    telefon TEXT
);

CREATE TABLE tranzactii (
    id TEXT PRIMARY KEY,
    id_client TEXT NOT NULL,
    data_tranzactie TEXT NOT NULL,
    FOREIGN KEY (id_client) REFERENCES clienti(email)
);

CREATE TABLE bilete (
    id_bilet TEXT PRIMARY KEY,
    id_tranzactie TEXT NOT NULL,
    id_eveniment TEXT NOT NULL,
    tip_acces TEXT NOT NULL,
    pret REAL NOT NULL,
    FOREIGN KEY (id_tranzactie) REFERENCES tranzactii(id),
    FOREIGN KEY (id_eveniment) REFERENCES evenimente(id)
);
