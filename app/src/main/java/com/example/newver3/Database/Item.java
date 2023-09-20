package com.example.newver3.Database;

import java.io.Serializable;

public class Item implements Serializable {
    private String title;
    private String url;
    private String imageUrl;
    private String date;
    private int nguon;

    public Item() {
    }

    public Item(String title, String url, String imageUrl,String date,int nguon) {
        this.title = title;
        this.url = url;
        this.imageUrl = imageUrl;
        this.date=date;
        this.nguon=nguon;

    }

    public int getNguon() {
        return nguon;
    }

    public void setNguon(int nguon) {
        this.nguon = nguon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getUrlNotRegex()
    {
        String s=url;
        s=s.replace(".","");
        s=s.replace("#","");
        s=s.replace("\\$","");
        s=s.replace("\\[","");
        s=s.replace("]","");
        s=s.replace(":","");
        s=s.replace("/","");
        return s;
    }

}

