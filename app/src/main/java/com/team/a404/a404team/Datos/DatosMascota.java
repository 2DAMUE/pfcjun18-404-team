package com.team.a404.a404team.Datos;

public class DatosMascota extends Marcadores_perdidos {
    private String nombre;
    private String rasgos;
    private String raza;
    private String url_foto;
    private String marker_id;


    public DatosMascota() { }

    public DatosMascota(String descripcion, String nombre, String raza, String url_foto,String marker_id) {
        this.nombre = nombre;
        this.rasgos = descripcion;
        this.raza = raza;
        this.url_foto = url_foto;
        this.marker_id = marker_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRasgos() {
        return rasgos;
    }

    public void setRasgos(String descripcion) {
        this.rasgos = descripcion;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }

    public String getMarker_id() {
        return marker_id;
    }

    public void setMarker_id(String marker_id) {
        this.marker_id = marker_id;
    }



}
