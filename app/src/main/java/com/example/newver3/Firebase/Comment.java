package com.example.newver3.Firebase;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Comment {
    public String username;
    public String name;
    public String comment;
    public String date;

    public Comment() {
    }

    public Comment(String username, String comment,String name,String date) {
        this.username = username;
        this.comment = comment;

        this.name=name;
        this.date=date;
    }

}
