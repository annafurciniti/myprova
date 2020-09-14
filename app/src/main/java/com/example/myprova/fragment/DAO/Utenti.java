package com.example.myprova.fragment.DAO;

public class Utenti {
    private String username;
    private String password;
    private int isAdmin;

    public Utenti( String username, String password, int ruolo) {
        this.username = username;
        this.password = password;
        this.isAdmin = ruolo;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public int getIsAdmin() { return isAdmin; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }


    public String toString() {
        return username + ", " + password + ", " + isAdmin;
    }
}
