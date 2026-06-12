package com.example.lab_mobile;

import android.content.Intent;
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
import com.example.lab_mobile.models.UnsplashPhoto;
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

    private int currentPage = 1;
    private boolean isLoading = false;
    private static final int PAGE_SIZE = 15;

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
        
        exploreAdapter = new ExploreAdapter(new ArrayList<>(), post -> {
            Intent intent = new Intent(requireActivity(), PostDetailActivity.class);

            // Format username
            String author = post.getAuthor();
            String username = "user_unknown";
            if (author != null && !author.isEmpty()) {
                username = author.toLowerCase().replaceAll("[^a-zA-Z0-9]", "_").replaceAll("_+", "_");
                if (username.endsWith("_")) {
                    username = username.substring(0, username.length() - 1);
                }
            }

            // Select deterministic caption
            String[] mockCaptions = {
                "Menikmati keindahan alam hari ini. 🌿✨",
                "Kopi hangat di sore hari yang dingin. ☕️🌧️",
                "Momen kecil, memori besar. 📸❤️",
                "Fokus pada proses, bukan hanya tujuan. 💪",
                "Kembali berpetualang mencari inspirasi. 🗺️🚶‍♂️",
                "Keindahan ada di sekitar kita jika kita mau melihatnya. 🌸",
                "Senja selalu punya cara untuk menenangkan hati. 🌅",
                "Hari baru, petualangan baru. Mari mulai! 🚀",
                "Hanya getaran positif di sini. ✌️😊",
                "Menjelajahi sudut kota yang belum terjamah. 🏢🔍",
                "Makan enak, hati senang. 🍕😋",
                "Bekerja keras dalam diam, biarkan kesuksesan bersuara. 💼",
                "Belajar dari kemarin, hidup untuk hari ini, berharap untuk besok. 🌟",
                "Menyatu dengan alam dan ketenangan. 🌲🏕️"
            };
            String postId = post.getId();
            int captionIndex = 0;
            if (postId != null) {
                captionIndex = Math.abs(postId.hashCode()) % mockCaptions.length;
            }
            String captionText = mockCaptions[captionIndex];

            intent.putExtra("username", username);
            intent.putExtra("caption", captionText);
            intent.putExtra("post_image_url", post.getCustomUrl(600, 600));
            intent.putExtra("profile_image_url", post.getCustomUrl(100, 100));

            startActivity(intent);
        });
        rvExploreGrid.setAdapter(exploreAdapter);

        rvExploreGrid.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && !isLoading) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        fetchExplorePosts();
                    }
                }
            }
        });

        fetchExplorePosts();
    }

    private void fetchExplorePosts() {
        if (isLoading) return;
        isLoading = true;

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<UnsplashPhoto>> call = apiService.getPosts(RetrofitClient.ACCESS_KEY, currentPage, PAGE_SIZE);

        call.enqueue(new Callback<List<UnsplashPhoto>>() {
            @Override
            public void onResponse(Call<List<UnsplashPhoto>> call, Response<List<UnsplashPhoto>> response) {
                isLoading = false;
                if (response.isSuccessful() && response.body() != null) {
                    List<UnsplashPhoto> unsplashPhotos = response.body();
                    List<Post> posts = new ArrayList<>();
                    for (UnsplashPhoto up : unsplashPhotos) {
                        posts.add(up.toPost());
                    }

                    if (currentPage == 1) {
                        exploreAdapter.setPosts(posts);
                    } else {
                        exploreAdapter.addPosts(posts);
                    }

                    currentPage++;
                }
            }

            @Override
            public void onFailure(Call<List<UnsplashPhoto>> call, Throwable t) {
                isLoading = false;
                Toast.makeText(getContext(), "Gagal memuat Explore", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
