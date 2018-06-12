package com.team.a404.a404team.Datos;

public class UserInformation {
    private String username;
    private String phonenumber;
    private String urlphoto;
    private String email;
    private String nombre;

    public  UserInformation(){}

    public UserInformation(String username, String phonenumber, String urlphoto, String email, String nombre) {
        this.username = username;
        this.phonenumber = phonenumber;
        this.urlphoto = urlphoto;
        this.email = email;
        this.nombre = nombre;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUrlphoto() {
        return urlphoto;
    }

    public void setUrlphoto(String urlphoto) {
        this.urlphoto = urlphoto;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
