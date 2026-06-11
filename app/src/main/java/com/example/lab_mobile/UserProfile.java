package com.example.lab_mobile;

public class UserProfile {
    private String nama;
    private String username;
    private String bio;
    private String jenisKelamin;
    private int fotoResId;
    private int followers;
    private int following;
    private int posts;


    // CONSTRUCTOR: Untuk membuat objek baru
    public UserProfile(String nama, String username, String bio, String jenisKelamin, int fotoResId, int followers, int following, int posts) {
        this.nama = nama;
        this.username = username;
        this.bio = bio;
        this.jenisKelamin = jenisKelamin;
        this.fotoResId = fotoResId;
        this.followers = followers;
        this.following = following;
        this.posts = posts;
    }

    // GETTER: Untuk mengambil data dari objek
    public String getNama() { return nama; }
    public String getUsername() { return username; }
    public String getBio() { return bio; }
    public String getJenisKelamin() { return jenisKelamin; }
    public int getFotoResId() { return fotoResId; }
    public int getFollowers() { return followers; }
    public int getFollowing() { return following; }
    public int getPosts() { return posts; }


    // SETTER: Untuk mengubah data di dalam objek
    public void setNama(String nama) { this.nama = nama; }
    public void setUsername(String username) { this.username = username; }
    public void setBio(String bio) { this.bio = bio; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }
    public void setFotoResId(int fotoResId) { this.fotoResId = fotoResId; }

}