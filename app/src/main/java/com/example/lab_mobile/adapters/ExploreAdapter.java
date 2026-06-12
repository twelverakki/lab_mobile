package com.example.lab_mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab_mobile.R;
import com.example.lab_mobile.models.Post;

import java.util.List;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Post post);
    }

    private List<Post> postList;
    private OnItemClickListener listener;

    public ExploreAdapter(List<Post> postList) {
        this.postList = postList;
    }

    public ExploreAdapter(List<Post> postList, OnItemClickListener listener) {
        this.postList = postList;
        this.listener = listener;
    }

    public void setPosts(List<Post> posts) {
        this.postList = posts;
        notifyDataSetChanged();
    }

    public void addPosts(List<Post> newPosts) {
        if (newPosts == null || newPosts.isEmpty()) return;
        int startPosition = this.postList.size();
        this.postList.addAll(newPosts);
        notifyItemRangeInserted(startPosition, newPosts.size());
    }

    @NonNull
    @Override
    public ExploreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore, parent, false);
        return new ExploreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreViewHolder holder, int position) {
        Post post = postList.get(position);
        
        // Load customized smaller size image (300x300 px) for Grid view
        Glide.with(holder.itemView.getContext())
                .load(post.getCustomUrl(300, 300))
                .placeholder(R.drawable.profile)
                .into(holder.ivExploreImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList == null ? 0 : postList.size();
    }

    static class ExploreViewHolder extends RecyclerView.ViewHolder {
        ImageView ivExploreImage;

        public ExploreViewHolder(@NonNull View itemView) {
            super(itemView);
            ivExploreImage = itemView.findViewById(R.id.iv_explore_image);
        }
    }
}
