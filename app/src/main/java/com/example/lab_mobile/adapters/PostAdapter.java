package com.example.lab_mobile.adapters;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab_mobile.R;
import com.example.lab_mobile.models.Post;

import java.util.List;
import java.util.Random;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private final Random random = new Random();

    private final String[] mockCaptions = {
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

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
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
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        
        // Generate username from author name
        String author = post.getAuthor();
        String username = "user_unknown";
        if (author != null && !author.isEmpty()) {
            username = author.toLowerCase().replaceAll("[^a-zA-Z0-9]", "_").replaceAll("_+", "_");
            if (username.endsWith("_")) {
                username = username.substring(0, username.length() - 1);
            }
        }
        
        int likes = random.nextInt(1000) + 10;
        
        holder.tvUsername.setText(username); 
        
        // Select caption deterministically based on post ID
        String postId = post.getId();
        int captionIndex = 0;
        if (postId != null) {
            captionIndex = Math.abs(postId.hashCode()) % mockCaptions.length;
        }
        String captionText = mockCaptions[captionIndex];
        
        // Bold username in caption
        String captionHtml = "<b>" + username + "</b> " + captionText;
        holder.tvCaption.setText(Html.fromHtml(captionHtml, Html.FROM_HTML_MODE_COMPACT));
        
        holder.tvLikes.setText(likes + " suka");

        // Load post image (600x600 px)
        Glide.with(holder.itemView.getContext())
                .load(post.getCustomUrl(600, 600))
                .placeholder(R.color.black)
                .into(holder.ivPostImage);
                
        // Use a smaller cropped image (100x100 px) as profile picture
        Glide.with(holder.itemView.getContext())
                .load(post.getCustomUrl(100, 100))
                .placeholder(R.drawable.profile)
                .into(holder.ivPostProfile);
    }

    @Override
    public int getItemCount() {
        return postList == null ? 0 : postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvCaption, tvLikes, tvTime;
        ImageView ivPostImage, ivPostProfile;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_post_username);
            tvCaption = itemView.findViewById(R.id.tv_post_caption);
            tvLikes = itemView.findViewById(R.id.tv_post_likes);
            tvTime = itemView.findViewById(R.id.tv_post_time);
            ivPostImage = itemView.findViewById(R.id.iv_post_image);
            ivPostProfile = itemView.findViewById(R.id.iv_post_profile);
        }
    }
}
