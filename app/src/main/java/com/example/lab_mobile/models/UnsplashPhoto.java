package com.example.lab_mobile.models;

import com.google.gson.annotations.SerializedName;

public class UnsplashPhoto {
    @SerializedName("id")
    private String id;

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;

    @SerializedName("user")
    private User user;

    @SerializedName("urls")
    private Urls urls;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Urls getUrls() { return urls; }
    public void setUrls(Urls urls) { this.urls = urls; }

    public static class User {
        @SerializedName("name")
        private String name;

        @SerializedName("username")
        private String username;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }

    public static class Urls {
        @SerializedName("raw")
        private String raw;

        @SerializedName("regular")
        private String regular;

        public String getRaw() { return raw; }
        public void setRaw(String raw) { this.raw = raw; }

        public String getRegular() { return regular; }
        public void setRegular(String regular) { this.regular = regular; }
    }

    public Post toPost() {
        Post post = new Post();
        post.setId(this.id != null ? this.id : "");
        post.setAuthor(this.user != null ? this.user.getName() : "Unknown");
        post.setWidth(this.width);
        post.setHeight(this.height);
        post.setUrl(this.urls != null ? this.urls.getRegular() : "");
        post.setDownloadUrl(this.urls != null ? this.urls.getRaw() : "");
        return post;
    }
}
