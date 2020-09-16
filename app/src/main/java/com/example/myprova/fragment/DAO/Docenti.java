package com.example.myprova.fragment.DAO;

public class Docenti {
    private String nome;


    public Docenti( String nome) {// int id_docente
        this.nome = nome;
    }


    public String getNome() {return nome;}

    public void setNome(String nome) { this.nome=nome; }

    public String toString() {
        return nome;
    }

}
