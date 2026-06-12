package com.example.lab_mobile.network;

import com.example.lab_mobile.models.UnsplashPhoto;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("photos")
    Call<List<UnsplashPhoto>> getPosts(
            @Query("client_id") String clientId,
            @Query("page") int page,
            @Query("per_page") int perPage
    );
}
