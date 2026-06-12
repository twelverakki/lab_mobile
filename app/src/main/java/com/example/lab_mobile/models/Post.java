package com.example.lab_mobile.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "posts")
public class Post {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private String id = "";

    @SerializedName("author")
    private String author;

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;

    @SerializedName("url")
    private String url;

    @SerializedName("download_url")
    private String downloadUrl;

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }

    // Helper method to get dynamically resized image URL to optimize memory usage
    public String getCustomUrl(int w, int h) {
        if (downloadUrl != null && downloadUrl.contains("unsplash.com")) {
            if (downloadUrl.contains("?")) {
                return downloadUrl + "&w=" + w + "&h=" + h + "&fit=crop";
            } else {
                return downloadUrl + "?w=" + w + "&h=" + h + "&fit=crop";
            }
        }
        return "https://picsum.photos/id/" + id + "/" + w + "/" + h;
    }
}
