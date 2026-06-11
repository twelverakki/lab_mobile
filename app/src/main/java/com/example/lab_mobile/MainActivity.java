package com.example.lab_mobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.imageview.ShapeableImageView;

public class MainActivity extends AppCompatActivity {

    private TextView tvName, tvBio, tvUsernameTop, tvPosts, tvFollowers, tvFollowing;
    private ShapeableImageView ivProfileMain;
    private ImageView ivPost1, ivPost2, ivPost3;
    private Uri currentProfileImageUri;
    public UserProfile userChandra;

    private final ActivityResultLauncher<Intent> editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String newName = result.getData().getStringExtra("name");
                    String newUsername = result.getData().getStringExtra("username");
                    String newBio = result.getData().getStringExtra("bio");
                    String newImageUriString = result.getData().getStringExtra("image_uri");
                    String newGender = result.getData().getStringExtra("gender");

                    if (newName != null) userChandra.setNama(newName);
                    if (newUsername != null) userChandra.setUsername(newUsername);
                    if (newBio != null) userChandra.setBio(newBio);
                    if (newGender != null) userChandra.setJenisKelamin(newGender);

                    tvName.setText(userChandra.getNama());
                    tvBio.setText(userChandra.getBio());
                    tvUsernameTop.setText(userChandra.getUsername());
                    
                    if (newImageUriString != null) {
                        currentProfileImageUri = Uri.parse(newImageUriString);
                        ivProfileMain.setImageURI(currentProfileImageUri);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        userChandra = new UserProfile(
                "chandra ",
                "chndr_andy",
                "confused",
                "Memilih tidak memberitahu",
                R.drawable.profile,
                574,
                515,
                3
        );

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvName = findViewById(R.id.tv_name);
        tvBio = findViewById(R.id.tv_bio);
        tvUsernameTop = findViewById(R.id.tv_username_top);
        
        // Sesuaikan ID dengan activity_main.xml (tv_posts, tv_followers, tv_following)
        tvPosts = findViewById(R.id.tv_posts);
        tvFollowers = findViewById(R.id.tv_followers);
        tvFollowing = findViewById(R.id.tv_following);
        
        Button btnEditProfile = findViewById(R.id.btn_edit_profile);
        ivProfileMain = findViewById(R.id.iv_profile_main);
        ivPost1 = findViewById(R.id.iv_post_1);
        ivPost2 = findViewById(R.id.iv_post_2);
        ivPost3 = findViewById(R.id.iv_post_3);

        tvName.setText(userChandra.getNama());
        tvBio.setText(userChandra.getBio());
        tvUsernameTop.setText(userChandra.getUsername());
        ivProfileMain.setImageResource(userChandra.getFotoResId());
        
        if (tvPosts != null) tvPosts.setText(String.valueOf(userChandra.getPosts()));
        if (tvFollowers != null) tvFollowers.setText(String.valueOf(userChandra.getFollowers()));
        if (tvFollowing != null) tvFollowing.setText(String.valueOf(userChandra.getFollowing()));

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NextActivity.class);
            intent.putExtra("current_name", userChandra.getNama());
            intent.putExtra("current_username", userChandra.getUsername());
            intent.putExtra("current_bio", userChandra.getBio());
            intent.putExtra("current_gender", userChandra.getJenisKelamin());
            
            if (currentProfileImageUri != null) {
                intent.putExtra("current_image_uri", currentProfileImageUri.toString());
            } else {
                intent.putExtra("current_image_res", userChandra.getFotoResId());
            }
            editProfileLauncher.launch(intent);
        });

        setupPostClick(ivPost1, R.drawable.foto2, "Mengabadikan sebuah moment itu penting, kenapa? karena di setiap potretan ada kenangan yang tidak bisa kita ulang kembali.");
        setupPostClick(ivPost2, R.drawable.foto3, "Kesempatan tidak datang dua kali.");
        setupPostClick(ivPost3, R.drawable.foto4, "Profil saya.");
    }

    private void setupPostClick(ImageView iv, int postImageResId, String caption) {
        iv.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PostDetailActivity.class);
            if (currentProfileImageUri != null) {
                intent.putExtra("profile_image_uri", currentProfileImageUri.toString());
            } else {
                intent.putExtra("profile_image_res", userChandra.getFotoResId());
            }
            intent.putExtra("post_image_res_id", postImageResId);
            intent.putExtra("username", userChandra.getUsername());
            intent.putExtra("caption", caption);
            startActivity(intent);
        });
    }
}
