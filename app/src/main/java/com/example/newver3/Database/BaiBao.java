package com.example.newver3.Database;

import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Blob;

@Entity(tableName = "baiBaos")
public class BaiBao implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "image")
    private  String image;
    @ColumnInfo(name = "datePost")
    private String datePost;
    @ColumnInfo(name="downLoadnguon")
    private String downLoadnguon;
    @ColumnInfo(name="linkNguon")
    private String linkNguon;

    public BaiBao() {
    }

    public BaiBao(String title, String image, String datePost, String downLoadnguon,String linkNguon) {
        this.title = title;
        this.image = image;

        this.datePost = datePost;
        this.downLoadnguon = downLoadnguon;
        this.linkNguon=linkNguon;

    }



    public String getLinkNguon() {
        return linkNguon;
    }

    public void setLinkNguon(String linkNguon) {
        this.linkNguon = linkNguon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public String getDownLoadnguon() {
        return downLoadnguon;
    }

    public void setDownLoadnguon(String downLoadnguon) {
        this.downLoadnguon = downLoadnguon;
    }
}
