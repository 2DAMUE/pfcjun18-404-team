package com.team.a404.a404team.Datos;

public class Marcadores_info {

    private String id_mascota;
    private String owner;
    private int tipo;
    private String marcador;

    public Marcadores_info(String id_mascota, String owner, int tipo, String marcador) {

        this.id_mascota = id_mascota;
        this.owner = owner;
        this.tipo = tipo;
        this.marcador = marcador;
    }

    public Marcadores_info(Object tag) {
    }

    public String getId_mascota() {
        return id_mascota;
    }

    public void setId_mascota(String id_mascota) {
        this.id_mascota = id_mascota;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getMarcador() {
        return marcador;
    }

    public void setMarcador(String marcador) {
        this.marcador = marcador;
    }
}


