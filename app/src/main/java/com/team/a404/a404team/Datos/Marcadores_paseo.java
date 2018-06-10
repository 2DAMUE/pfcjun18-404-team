package com.team.a404.a404team.Datos;

public class Marcadores_paseo {
    private double latitud,longitud;
    private String tiempo_de, tiempo_hasta,owner;

    public Marcadores_paseo(){}

    public Marcadores_paseo(double latitud, double longitud, String tiempo_de, String tiempo_asta, String owner) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.tiempo_de = tiempo_de;
        this.tiempo_hasta = tiempo_asta;
        this.owner = owner;
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

    public String getTiempo_de() {
        return tiempo_de;
    }

    public void setTiempo_de(String tiempo_de) {
        this.tiempo_de = tiempo_de;
    }

    public String getTiempo_hasta() {
        return tiempo_hasta;
    }

    public void setTiempo_hasta(String tiempo_hasta) {
        this.tiempo_hasta = tiempo_hasta;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
