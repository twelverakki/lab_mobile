package com.example.lab_mobile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_mobile.adapters.PostAdapter;
import com.example.lab_mobile.database.AppDatabase;
import com.example.lab_mobile.models.Post;
import com.example.lab_mobile.network.ApiService;
import com.example.lab_mobile.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView rvHomeFeed;
    private Button btnRefresh;
    private PostAdapter postAdapter;
    private AppDatabase db;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvHomeFeed = view.findViewById(R.id.rv_home_feed);
        btnRefresh = view.findViewById(R.id.btn_refresh);

        rvHomeFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        postAdapter = new PostAdapter(new ArrayList<>());
        rvHomeFeed.setAdapter(postAdapter);

        db = AppDatabase.getInstance(requireContext());
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        btnRefresh.setOnClickListener(v -> fetchPostsFromApi());

        fetchPostsFromApi();
    }

    private void fetchPostsFromApi() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Post>> call = apiService.getPosts(20);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Post> posts = response.body();
                    postAdapter.setPosts(posts);
                    btnRefresh.setVisibility(View.GONE);
                    savePostsToDatabase(posts);
                } else {
                    handleNetworkFailure();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                handleNetworkFailure();
            }
        });
    }

    private void savePostsToDatabase(List<Post> posts) {
        executorService.execute(() -> {
            db.postDao().insertPosts(posts);
        });
    }

    private void handleNetworkFailure() {
        Toast.makeText(getContext(), "Koneksi gagal, memuat data offline...", Toast.LENGTH_SHORT).show();
        btnRefresh.setVisibility(View.VISIBLE);
        
        executorService.execute(() -> {
            List<Post> offlinePosts = db.postDao().getAllPosts();
            mainHandler.post(() -> {
                if (offlinePosts != null && !offlinePosts.isEmpty()) {
                    postAdapter.setPosts(offlinePosts);
                } else {
                    Toast.makeText(getContext(), "Tidak ada data tersimpan", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
