package com.example.newver3.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Likes {
    public String link;
    public String title;

    public Likes() {
    }

    public Likes(String link, String title) {
        this.link = link;
        this.title = title;
    }
}
