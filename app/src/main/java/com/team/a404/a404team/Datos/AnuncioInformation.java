package com.team.a404.a404team.Datos;

public class AnuncioInformation {
    private double latitud,longitud;
    private String nombre,descripcion;
    public AnuncioInformation(){}

    public AnuncioInformation(double latitud, double longitud, String nombre, String descripcion) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.descripcion = descripcion;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


}
