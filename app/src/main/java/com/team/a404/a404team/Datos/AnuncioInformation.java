package com.team.a404.a404team.Datos;

public class AnuncioInformation extends Marcadores_perdidos {
    private String nombre;
    private String descripcion;
    private String raza;
    private String url_foto;
    private String owner;

    public AnuncioInformation() { }

    public AnuncioInformation(String descripcion, String nombre, String raza, String owner,String url_foto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.raza = raza;
        this.url_foto = url_foto;
        this.owner = owner;
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

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public void setOwner(String owner) {
        this.owner = owner;
    }

}
