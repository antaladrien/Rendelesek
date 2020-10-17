package com.company;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void visszaAllitEredetiDb(ArrayList<Termek> termekek) {
        for (int i = 0; i < termekek.size(); i++) {
            termekek.get(i).db = termekek.get(i).eredetiDb;
        }
    }

    public static void veglegesitUjDb(ArrayList<Termek> termekek) {
        for (int i = 0; i < termekek.size(); i++) {
            termekek.get(i).eredetiDb = termekek.get(i).db;
        }
    }


    public static void main(String[] args) {
        String sor;
        ArrayList<Termek> termekek = new ArrayList<>();
        ArrayList<Ugyfel> ugyfelek = new ArrayList<>();

        try (FileReader fr = new FileReader("raktar.csv");
             BufferedReader br = new BufferedReader(fr)) {

            while ((sor = br.readLine()) != null ) {
                String[] valtozok = sor.split(";");
                Termek t = new Termek(valtozok[0],
                        valtozok[1], Integer.parseInt(valtozok[2]),
                        Integer.parseInt(valtozok[3]));
                termekek.add(t);
            }
        } catch (FileNotFoundException fne) {
            System.out.println(fne);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }


        try (FileReader fr = new FileReader("rendeles.csv");
             BufferedReader br = new BufferedReader(fr)) {

            while ( (sor = br.readLine()) != null ) {
                String[] valtozok = sor.split(";");

                if(valtozok[0].compareTo("M") == 0) {
                    String datum = valtozok[1];
                    int rendelesSzam = Integer.parseInt(valtozok[2]);
                    String email = valtozok[3];
                    ArrayList<Rendeles> rendelesek =
                            new ArrayList<>();

                    Ugyfel u = new Ugyfel(datum,
                            rendelesSzam, email,
                                    rendelesek);
                    ugyfelek.add(u);
                } else if(valtozok[0].compareTo("T") == 0) {
                    int rendelesSzam
                            = Integer.parseInt(valtozok[1]);
                    String kod = valtozok[2];
                    int db = Integer.parseInt(valtozok[3]);

                    Rendeles r = new Rendeles(rendelesSzam,
                            kod, db);
                    Ugyfel u = ugyfelek.get(ugyfelek.size()-1);
                    u.rendelesek.add(r);
                }
            }
        } catch (FileNotFoundException fne) {
            System.out.println(fne);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }


        HashMap<String, Integer> rendelesSzamok =
                new HashMap<>();
        for (int i = 0; i < ugyfelek.size(); i++) {
            for (int j = 0; j < ugyfelek.get(i).rendelesek.size(); j++) {
                Rendeles r = ugyfelek.get(i).rendelesek.get(j);
                if(rendelesSzamok.containsKey(r.kod)) {
                    int db = rendelesSzamok.get(r.kod);
                    db += r.db;
                    rendelesSzamok.replace(r.kod, db);
                } else {
                    rendelesSzamok.put(r.kod, r.db);
                }
            }
        }

        for (int i = 0; i < termekek.size(); i++) {
            int db = rendelesSzamok.get(termekek.get(i).kod);
            db -= termekek.get(i).db;
            rendelesSzamok.replace(termekek.get(i).kod, db);
        }

        try (FileWriter fw = new FileWriter("levelek.csv");
            BufferedWriter bw = new BufferedWriter(fw)) {

            for (int i = 0; i < ugyfelek.size(); i++) {
                double arak = 0.0;
                rendeleskezeles: for (int j = 0; j < ugyfelek.get(i).rendelesek.size(); j++) {
                    Rendeles r = ugyfelek.get(i).rendelesek.get(j);

                    for (int k = 0; k < termekek.size(); k++) {
                        if(termekek.get(k).kod.compareTo(r.kod) == 0) {
                            if(r.db <= termekek.get(k).db) {
                                termekek.get(k).db -= r.db;
                                arak += (r.db * termekek.get(k).ar);
                            } else {
                                ugyfelek.get(i).teljesitheto = false;
                                visszaAllitEredetiDb(termekek);
                                break rendeleskezeles;
                            }
                        }
                    }
                }
                if(ugyfelek.get(i).teljesitheto) {
                    veglegesitUjDb(termekek);
                    bw.write(ugyfelek.get(i).email + ";" + "A rendelését két napon belül szállítjuk. " +
                            "A rendelés értéke: " + arak);
                    bw.newLine();
                } else {
                    bw.write(ugyfelek.get(i).email + ";" + "A rendelése függő állapotba került, " +
                            "hamarosan értesítjük a szállítás időpontjáról!");
                }
            }

        } catch (FileNotFoundException fne) {
            System.out.println(fne);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

        try (FileWriter fw = new FileWriter("beszerzes.csv");
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (Map.Entry<String, Integer> element : rendelesSzamok.entrySet()) {
                if(element.getValue() > 0) {
                    bw.write(element.getKey() + ";" + element.getValue());
                    bw.newLine();
                }
            }

        } catch (FileNotFoundException fne) {
            System.out.println(fne);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }


    }
}
