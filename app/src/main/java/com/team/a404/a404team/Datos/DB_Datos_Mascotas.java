package com.team.a404.a404team.Datos;

public class DB_Datos_Mascotas {

    private String nombre;
    private String raza;
    private String rasgos;
    private String url_foto;
    private String marker_id;

    public DB_Datos_Mascotas() { }

    public DB_Datos_Mascotas(String nombre, String raza, String rasgos, String url_foto,String marker_id) {
        this.nombre = nombre;
        this.raza = raza;
        this.rasgos = rasgos;
        this.url_foto = url_foto;
        this.marker_id = marker_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getRasgos() {
        return rasgos;
    }

    public void setRasgos(String rasgos) {
        this.rasgos = rasgos;
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
