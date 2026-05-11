package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip)
        // 2. Scrie toate înregistrările în OUTPUT_FILE cu DataOutputStream (format binar, RECORD_SIZE=32 bytes/înreg.)
        //    - bytes 0-3:   id (int, little-endian via ByteBuffer)
        //    - bytes 4-11:  suma (double, little-endian via ByteBuffer)
        //    - bytes 12-21: data (String, 10 chars ASCII, paddat cu spații la dreapta)
        //    - byte 22:     tip (0=CREDIT, 1=DEBIT)
        //    - byte 23:     status (0=PENDING, 1=PROCESSED, 2=REJECTED)
        //    - bytes 24-31: padding (zerouri)
        // 3. Procesează comenzile din stdin până la EOF cu RandomAccessFile:
        //    - READ idx       → seek(idx * RECORD_SIZE), citește și afișează înregistrarea
        //    - UPDATE idx ST  → seek(idx * RECORD_SIZE + 23), scrie noul status (0/1/2)
        //                       afișează "Updated [idx]: STATUS"
        //    - PRINT_ALL      → citește și afișează toate înregistrările
        //
        // Format linie output:
        //   [idx] id=<id> data=<data> tip=<CREDIT|DEBIT> suma=<suma:.2f> RON status=<STATUS>

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        new File("output").mkdirs();
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (int i = 0; i < n; i++) {
                int id = scanner.nextInt();
                double suma = scanner.nextDouble();
                String data = scanner.next();
                TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

                byte[] record = new byte[RECORD_SIZE];
                ByteBuffer buffer = ByteBuffer.wrap(record).order(ByteOrder.LITTLE_ENDIAN);
                buffer.putInt(id);
                buffer.putDouble(suma);

                byte[] dataBytes = data.getBytes(StandardCharsets.US_ASCII);
                for (int j = 0; j < 10; j++) {
                    record[12 + j] = j < dataBytes.length ? dataBytes[j] : (byte) ' ';
                }

                record[22] = (byte) (tip == TipTranzactie.CREDIT ? 0 : 1);
                record[23] = 0;
                out.write(record);
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            while (scanner.hasNext()) {
                String comanda = scanner.next();
                if (comanda.equals("READ")) {
                    int idx = scanner.nextInt();
                    afiseaza(raf, idx);
                } else if (comanda.equals("UPDATE")) {
                    int idx = scanner.nextInt();
                    String status = scanner.next();
                    raf.seek((long) idx * RECORD_SIZE + 23);
                    raf.write(statusByte(status));
                    System.out.println("Updated [" + idx + "]: " + status);
                } else if (comanda.equals("PRINT_ALL")) {
                    for (int i = 0; i < n; i++) {
                        afiseaza(raf, i);
                    }
                }
            }
        }
    }

    private static void afiseaza(RandomAccessFile raf, int idx) throws IOException {
        byte[] record = new byte[RECORD_SIZE];
        raf.seek((long) idx * RECORD_SIZE);
        raf.readFully(record);

        ByteBuffer buffer = ByteBuffer.wrap(record).order(ByteOrder.LITTLE_ENDIAN);
        int id = buffer.getInt();
        double suma = buffer.getDouble();
        String data = new String(record, 12, 10, StandardCharsets.US_ASCII).trim();
        TipTranzactie tip = record[22] == 0 ? TipTranzactie.CREDIT : TipTranzactie.DEBIT;
        String status = statusText(record[23]);

        System.out.printf("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s%n",
                idx, id, data, tip, suma, status);
    }

    private static byte statusByte(String status) {
        if (status.equals("PROCESSED")) {
            return 1;
        }
        if (status.equals("REJECTED")) {
            return 2;
        }
        return 0;
    }

    private static String statusText(byte status) {
        if (status == 1) {
            return "PROCESSED";
        }
        if (status == 2) {
            return "REJECTED";
        }
        return "PENDING";
    }
}