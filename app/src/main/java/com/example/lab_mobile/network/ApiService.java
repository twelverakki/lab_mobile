package com.example.lab_mobile.network;

import com.example.lab_mobile.models.Post;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("v2/list")
    Call<List<Post>> getPosts(@Query("limit") int limit);
}
