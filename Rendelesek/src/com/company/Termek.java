package com.company;

public class Termek {
    String kod;
    String nev;
    int ar;
    int db;
    int eredetiDb;

    public Termek(String kod, String nev, int ar, int db) {
        this.kod = kod;
        this.nev = nev;
        this.ar = ar;
        this.db = db;
        this.eredetiDb = db;
    }
}
