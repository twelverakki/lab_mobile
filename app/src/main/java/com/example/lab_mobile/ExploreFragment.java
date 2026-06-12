package com.example.lab_mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_mobile.adapters.ExploreAdapter;
import com.example.lab_mobile.models.Post;
import com.example.lab_mobile.network.ApiService;
import com.example.lab_mobile.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreFragment extends Fragment {

    private RecyclerView rvExploreGrid;
    private ExploreAdapter exploreAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvExploreGrid = view.findViewById(R.id.rv_explore_grid);
        rvExploreGrid.setLayoutManager(new GridLayoutManager(getContext(), 3));
        
        exploreAdapter = new ExploreAdapter(new ArrayList<>());
        rvExploreGrid.setAdapter(exploreAdapter);

        fetchExplorePosts();
    }

    private void fetchExplorePosts() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        // Fetch different range for explore or same
        Call<List<Post>> call = apiService.getPosts(30);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    exploreAdapter.setPosts(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal memuat Explore", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
