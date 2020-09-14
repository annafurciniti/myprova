package com.example.myprova.fragment.DAO;

public class Docenti {
    private String nome;
    private String cognome;
    private int id_docente;


    public Docenti( String nome, String cognome, int id_docente ) {// int id_docente
        this.nome = nome;
        this.cognome = cognome;
        this.id_docente= id_docente;
    }


    public String getNome() {return nome;}
    public String getCognome() {
        return cognome;
    }
    public int getId_docente() {
        return id_docente;
    }

    public void setNome(String nome) { this.nome=nome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public void setid_docente(int id_docente) { this.id_docente = id_docente;}

    public String toString() {
        return nome + " " + cognome +" " + id_docente;
    }
}
