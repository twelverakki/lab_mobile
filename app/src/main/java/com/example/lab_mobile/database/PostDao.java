package com.example.lab_mobile.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.lab_mobile.models.Post;

import java.util.List;

@Dao
public interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPosts(List<Post> posts);

    @Query("SELECT * FROM posts LIMIT 30")
    List<Post> getAllPosts();

    @Query("DELETE FROM posts")
    void deleteAll();
}
