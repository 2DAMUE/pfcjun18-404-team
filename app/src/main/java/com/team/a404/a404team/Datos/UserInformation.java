package com.team.a404.a404team.Datos;

public class UserInformation {
    private String username,phonenumber,urlphoto;
    public  UserInformation(){}

    public UserInformation(String username, String phonenumber, String urlphoto) {
        this.username = username;
        this.phonenumber = phonenumber;
        this.urlphoto = urlphoto;
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


}
