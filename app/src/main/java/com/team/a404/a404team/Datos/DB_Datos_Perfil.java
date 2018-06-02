package com.team.a404.a404team.Datos;

public class DB_Datos_Perfil {

    private String email,nombre,urlfoto;

    public DB_Datos_Perfil() { }

    public DB_Datos_Perfil(String email, String nombre, String urlfoto) {
        this.email = email;
        this.nombre = nombre;
        this.urlfoto = urlfoto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlfoto() {
        return urlfoto;
    }

    public void setUrlfoto(String urlfoto) {
        this.urlfoto = urlfoto;
    }
}
