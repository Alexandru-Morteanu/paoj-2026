# Proiect PAO — Platformă E-Ticketing

Acest proiect reprezintă un sistem de gestiune pentru evenimente, locații și vânzări de bilete, dezvoltat ca parte a disciplinei Programare Avansată pe Obiecte (2026).

## 1. Definirea sistemului

### 1.1 — Lista de acțiuni / interogări posibile

1.  **Adăugare locație**: Înregistrarea unei noi locații în sistem (ex: stadioane, teatre, săli de concerte) cu o capacitate maximă definită.
2.  **Adăugare eveniment**: Crearea unui eveniment nou asociat unei locații și unei date calendaristice specifice.
3.  **Înregistrare client**: Adăugarea unui nou utilizator de tip client în baza de date a platformei.
4.  **Achiziție bilete**: Procesarea unei tranzacții prin care un client cumpără unul sau mai multe bilete pentru un eveniment disponibil.
5.  **Anulare bilet/tranzacție**: Returnarea biletelor și actualizarea automată a numărului de locuri disponibile pentru evenimentul respectiv.
6.  **Căutare eveniment după ID**: Interogarea sistemului pentru a obține detaliile complete ale unui eveniment specific.
7.  **Listare evenimente după locație**: Afișarea tuturor spectacolelor sau concertelor programate să aibă loc într-o anumită incintă.
8.  **Verificare disponibilitate**: Calcularea în timp real a locurilor rămase libere dintr-o locație pentru un anumit eveniment.
9.  **Afișare istoric client**: Vizualizarea tuturor tranzacțiilor și biletelor achiziționate de un utilizator de-a lungul timpului.
10. **Ordonare cronologică**: Afișarea calendarului de evenimente sortat automat de la cea mai apropiată dată la cea mai îndepărtată.

### 1.2 — Lista tipurilor de obiecte din domeniu

* **Bilet**: Clasificată ca **clasă imutabilă**, reprezintă dreptul de acces al unui client la un eveniment specific.
* **Client**: Tip de utilizator care poate achiziționa bilete și deține un istoric de tranzacții.
* **Eveniment**: Obiectul central care corelează o locație cu o dată, un nume de show și numărul de bilete vândute.
* **Locatie**: Entitate care reprezintă spațiul fizic unde se desfășoară evenimentele (conține nume, adresă și capacitate).
* **Organizator**: Tip de utilizator responsabil pentru gestionarea evenimentelor și a locațiilor.
* **Owner**: Definește tipul de acces (VIP, General), prețul și locurile disponibile specifice fiecărei secțiuni dintr-un eveniment.
* **Tranzactie**: Înregistrarea unei achiziții, care grupează mai multe bilete sub un singur identificator de plată.
* **Utilizator**: Clasă abstractă care definește atributele comune pentru persoanele din sistem (ID, nume, email).

---

## Etapa II — JDBC, tranzacții, audit

- **SQLite**: `resources/db.properties` + `schema.sql`
- **Repository-uri**: `Locatie`, `Client`, `Eveniment`, `Tranzactie`, `Bilet`
- **Tranzacții JDBC**: achiziție bilete (UPDATE eveniment + INSERT tranzacție + INSERT bilete), anulare tranzacție
- **JOIN-uri** (în `EvenimentRepository`): calendar cronologic, evenimente după locație, istoric client
- **Audit**: `audit.csv` (append, thread-safe cu `ReentrantLock`)

Rulare din rădăcina proiectului (necesită `lib/sqlite-jdbc.jar` și dependențele SLF4J din `.classpath`):

```bash
java -cp "output:lib/sqlite-jdbc.jar:lib/slf4j-api-2.0.13.jar:lib/slf4j-simple-2.0.13.jar:src" com.pao.project.eticketing.Main
```
