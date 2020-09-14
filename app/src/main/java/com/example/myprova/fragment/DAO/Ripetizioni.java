package com.example.myprova.fragment.DAO;


public class Ripetizioni {
    private String stato;
    private int giorno;
    private int ora_i;
    private String id_corso;//titolo
    private String id_docente;
    private String username;


    public Ripetizioni( String stato, int giorno, int ora_i, String id_corso, String id_docente, String username) {
        this.stato = stato;
        this.giorno = giorno;
        this.ora_i = ora_i;
        this.id_corso = id_corso;
        this.id_docente = id_docente;
        this.username = username;
    }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public int getOra_i() { return ora_i; }
    public void setOra_i(int ora_i) { this.ora_i = ora_i; }

    public String getId_corso() { return id_corso; }
    public void setCorso(String corso) { this.id_corso = id_corso; }

    public String getId_docente() { return id_docente; }
    public void setId_docente(String id_docente) { this.id_docente = id_docente; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getGiorno() { return giorno; }
    public void setGiorno(int giorno) { this.giorno = giorno; }

    public String toString() {
        return giorno+ " " + ora_i +" "+ id_corso+ " "+ id_docente;
    }
}