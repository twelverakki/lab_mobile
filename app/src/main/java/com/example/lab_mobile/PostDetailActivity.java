package com.example.lab_mobile;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PostDetailActivity extends AppCompatActivity {

    private ImageView ivPostImage, btnBack, ivPostProfile;
    private TextView tvUsernameTitle, tvPostUsername, tvPostCaption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_detail);

        // Cari root view untuk padding (menggunakan ID header karena main tidak ada)
        View rootView = findViewById(R.id.header);
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        ivPostImage = findViewById(R.id.iv_post_image);
        ivPostProfile = findViewById(R.id.iv_post_profile);
        btnBack = findViewById(R.id.btn_back);
        tvUsernameTitle = findViewById(R.id.tv_username_title);
        tvPostUsername = findViewById(R.id.tv_post_username);
        tvPostCaption = findViewById(R.id.tv_post_caption);

        // Ambil data dari intent
        String profileImageUriString = getIntent().getStringExtra("profile_image_uri");
        int profileImageRes = getIntent().getIntExtra("profile_image_res", -1);
        int postImageResId = getIntent().getIntExtra("post_image_res_id", R.drawable.foto2);
        String username = getIntent().getStringExtra("username");
        String caption = getIntent().getStringExtra("caption");

        // Set Foto Profil (Dinamis)
        if (profileImageUriString != null) {
            ivPostProfile.setImageURI(Uri.parse(profileImageUriString));
        } else if (profileImageRes != -1) {
            ivPostProfile.setImageResource(profileImageRes);
        }

        // Set Foto Postingan
        ivPostImage.setImageResource(postImageResId);

        // Set Text
        tvUsernameTitle.setText(username);
        tvPostUsername.setText(username);
        tvPostCaption.setText(caption);

        btnBack.setOnClickListener(v -> finish());
    }
}
