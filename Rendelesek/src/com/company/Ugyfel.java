package com.company;

import java.util.ArrayList;

public class Ugyfel {
    String datum;
    int rendelesSzam;
    String email;
    ArrayList<Rendeles> rendelesek;
    boolean teljesitheto = true;

    public Ugyfel(String datum, int rendelesSzam, String email, ArrayList<Rendeles> rendelesek) {
        this.datum = datum;
        this.rendelesSzam = rendelesSzam;
        this.email = email;
        this.rendelesek = rendelesek;
    }
}
