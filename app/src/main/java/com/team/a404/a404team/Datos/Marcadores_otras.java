package com.team.a404.a404team.Datos;

public class Marcadores_otras {
   private String rasgos;
    private String url_foto;
    private String marker_id;
    private double latitud,longitud;
    private String telefono;


    public Marcadores_otras(){
    }

    public Marcadores_otras(String rasgos, String url_foto, double latitud, double longitud,String marker_id,String telefono) {
        this.rasgos = rasgos;
        this.url_foto = url_foto;
        this.latitud = latitud;
        this.longitud = longitud;
        this.marker_id = marker_id;
        this.telefono = telefono;
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

    public String getMarker_id() {
        return marker_id;
    }

    public void setMarker_id(String marker_id) {
        this.marker_id = marker_id;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


}
