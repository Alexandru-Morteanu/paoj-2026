package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip) — pot exista duplicate de id
        //    Stochează-le toate într-un ArrayList<Tranzactie> (cu duplicate, ordine inserare)
        //
        // 2. Procesează comenzile din stdin până la EOF:
        //
        //   UNIQUE_IDS      → LinkedHashSet<Integer> cu id-urile în ordinea primei apariții
        //                     afișează: "IDs unice (N): [1, 2, 3, ...]"
        //
        //   MONTHLY_REPORT  → TreeMap<String, ...> grupat pe yyyy-MM (substring 0-7 din data)
        //                     pentru fiecare lună, sumele CREDIT și DEBIT
        //                     format: "yyyy-MM: CREDIT X.XX RON, DEBIT Y.YY RON"
        //
        //   TOP n           → primele n tranzacții după suma descrescătoare (nu modifică lista)
        //                     afișează "Top n:" urmat de n linii
        //
        //   SORT_ASC        → Collections.sort cu suma crescătoare; afișează lista sortată
        //   SORT_DESC       → Collections.sort cu suma descrescătoare; afișează lista sortată
        //   REVERSE         → Collections.reverse; afișează lista
        //   MIN_MAX         → Collections.min/max după suma
        //                     "MIN: [id] data tip: suma RON"
        //                     "MAX: [id] data tip: suma RON"
        //
        //   CME_DEMO        → încearcă for(t : lista) lista.remove(t) în try-catch
        //                     afișează "ConcurrentModificationException prins: modificare in iteratie detectata."
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON

        Scanner sc = new Scanner(System.in);

        // Citește N
        int N = Integer.parseInt(sc.nextLine().trim());
        List<Tranzactie> lista = new ArrayList<>();

        // Citește cele N tranzacții cu nextLine()
        for (int i = 0; i < N; i++) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                i--;
                continue;
            }

            String[] p = line.split("\\s+");
            int id = Integer.parseInt(p[0]);
            double suma = Double.parseDouble(p[1]);
            String data = p[2];
            TipTranzactie tip = TipTranzactie.valueOf(p[3]);

            lista.add(new Tranzactie(id, suma, data, tip));
        }

        // Comenzi până la EOF
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String cmd = parts[0];

            switch (cmd) {

                case "UNIQUE_IDS": {
                    LinkedHashSet<Integer> set = new LinkedHashSet<>();
                    for (Tranzactie t : lista) set.add(t.getId());

                    System.out.print("IDs unice (" + set.size() + "): [");
                    boolean first = true;
                    for (int id : set) {
                        if (!first) System.out.print(", ");
                        System.out.print(id);
                        first = false;
                    }
                    System.out.println("]");
                    break;
                }

                case "MONTHLY_REPORT": {
                    TreeMap<String, double[]> map = new TreeMap<>();

                    for (Tranzactie t : lista) {
                        String luna = t.getData().substring(0, 7);
                        map.putIfAbsent(luna, new double[]{0.0, 0.0});

                        if (t.getTip() == TipTranzactie.CREDIT)
                            map.get(luna)[0] += t.getSuma();
                        else
                            map.get(luna)[1] += t.getSuma();
                    }

                    for (var e : map.entrySet()) {
                        System.out.printf(Locale.US,
                                "%s: CREDIT %.2f RON, DEBIT %.2f RON%n",
                                e.getKey(), e.getValue()[0], e.getValue()[1]);
                    }
                    break;
                }

                case "TOP": {
                    int n = Integer.parseInt(parts[1]);

                    List<Tranzactie> copie = new ArrayList<>(lista);
                    copie.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());

                    System.out.println("Top " + n + ":");
                    for (int i = 0; i < Math.min(n, copie.size()); i++)
                        System.out.println(copie.get(i));
                    break;
                }

                case "SORT_ASC": {
                    lista.sort(Comparator.comparingDouble(Tranzactie::getSuma));
                    for (Tranzactie t : lista) System.out.println(t);
                    break;
                }

                case "SORT_DESC": {
                    lista.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    for (Tranzactie t : lista) System.out.println(t);
                    break;
                }

                case "REVERSE": {
                    Collections.reverse(lista);
                    for (Tranzactie t : lista) System.out.println(t);
                    break;
                }

                case "MIN_MAX": {
                    Tranzactie min = Collections.min(lista, Comparator.comparingDouble(Tranzactie::getSuma));
                    Tranzactie max = Collections.max(lista, Comparator.comparingDouble(Tranzactie::getSuma));

                    System.out.println("MIN: " + min);
                    System.out.println("MAX: " + max);
                    break;
                }

                case "CME_DEMO": {
                    try {
                        for (Tranzactie t : lista) {
                            lista.remove(t);
                        }
                    } catch (ConcurrentModificationException e) {
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
                    break;
                }
            }
        }
    }
}