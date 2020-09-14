package com.example.myprova.fragment.DAO;

public class Corso {
    private String titolo;
    private String descrizione;


    public Corso(String titolo, String descrizione) {
        this.titolo=titolo;
        this.descrizione=descrizione;
    }

    public String getTitolo() { return titolo; }
    public String getDescrizione() { return descrizione; }


    public void setTitolo(String titolo) { this.titolo = titolo; }
    public void setDescrizione(String descr) { this.descrizione = descr; }

    @Override
    public String toString() { return titolo + ", " + descrizione;}
}
