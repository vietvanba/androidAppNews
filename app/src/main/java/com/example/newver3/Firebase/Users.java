package com.example.newver3.Firebase;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

@IgnoreExtraProperties
public class Users implements Serializable {
    public String username;
    public String password;
    public String name;
    public String dateOfBirth;
    public String dateCreateAccount;
    public String mail;
    public boolean isAdmin;
    public List<Boolean> theloai;
    public String avatar;
    public Users() {
    }

    public Users(String username, String password, String name, String dateOfBirth, String dateCreateAccount, String mail, boolean isAdmin, List<Boolean> theloai,String avatar) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.dateCreateAccount = dateCreateAccount;
        this.mail = mail;
        this.isAdmin = isAdmin;
        this.theloai = theloai;
        this.avatar=avatar;
    }
}
