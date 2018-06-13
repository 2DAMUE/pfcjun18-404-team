package com.team.a404.a404team.Datos;

public class Marcadores_paseo {
    private double latitud,longitud;
    private String owner,comentario;

    public Marcadores_paseo(){}

    public Marcadores_paseo(double latitud, double longitud, String owner, String comentario) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.owner = owner;
        this.comentario = comentario;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getComentario() {return comentario;
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
