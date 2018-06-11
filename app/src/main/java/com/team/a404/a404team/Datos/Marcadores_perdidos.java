package com.team.a404.a404team.Datos;

public class Marcadores_perdidos {
    private double latitud, longitud;
    private String id_mascota;
    private String owner;
    private String telefono;

    public Marcadores_perdidos() {
    }

    public Marcadores_perdidos(String id_mascota, double latitud, double longitud, String owner, String telefono) {
        this.id_mascota = id_mascota;
        this.latitud = latitud;
        this.longitud = longitud;
        this.owner = owner;
        this.telefono = telefono;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
