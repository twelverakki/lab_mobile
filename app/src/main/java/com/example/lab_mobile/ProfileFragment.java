package com.example.lab_mobile;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.imageview.ShapeableImageView;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvBio, tvUsernameTop, tvPosts, tvFollowers, tvFollowing;
    private ShapeableImageView ivProfileMain;
    private ImageView ivPost1, ivPost2, ivPost3;
    private Button btnShareProfile;
    private LinearLayout llHighlight;
    private Uri currentProfileImageUri;
    public UserProfile userChandra;

    private ActivityResultLauncher<Intent> editProfileLauncher;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        sharedPreferences = requireActivity().getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE);
        
        // Load dari SharedPreferences, gunakan nilai default jika belum ada
        String savedName = sharedPreferences.getString("name", "chandra ");
        String savedUsername = sharedPreferences.getString("username", "chndr_andy");
        String savedBio = sharedPreferences.getString("bio", "Memilih tidak memberitahu");
        String savedGender = sharedPreferences.getString("gender", "confused");
        String savedImageUri = sharedPreferences.getString("image_uri", null);

        userChandra = new UserProfile(
                savedName,
                savedUsername,
                savedGender,
                savedBio,
                R.drawable.profile,
                574,
                515,
                3
        );
        
        if (savedImageUri != null) {
            currentProfileImageUri = Uri.parse(savedImageUri);
        }

        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String newName = result.getData().getStringExtra("name");
                        String newUsername = result.getData().getStringExtra("username");
                        String newBio = result.getData().getStringExtra("bio");
                        String newImageUriString = result.getData().getStringExtra("image_uri");
                        String newGender = result.getData().getStringExtra("gender");

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        if (newName != null) {
                            userChandra.setNama(newName);
                            editor.putString("name", newName);
                        }
                        if (newUsername != null) {
                            userChandra.setUsername(newUsername);
                            editor.putString("username", newUsername);
                        }
                        if (newBio != null) {
                            userChandra.setBio(newBio);
                            editor.putString("bio", newBio);
                        }
                        if (newGender != null) {
                            userChandra.setJenisKelamin(newGender);
                            editor.putString("gender", newGender);
                        }

                        tvName.setText(userChandra.getNama());
                        tvBio.setText(userChandra.getBio());
                        tvUsernameTop.setText(userChandra.getUsername());

                        if (newImageUriString != null) {
                            currentProfileImageUri = Uri.parse(newImageUriString);
                            ivProfileMain.setImageURI(currentProfileImageUri);
                            editor.putString("image_uri", newImageUriString);
                        }
                        
                        // Simpan perubahan ke local storage
                        editor.apply();
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.tv_name);
        tvBio = view.findViewById(R.id.tv_bio);
        tvUsernameTop = view.findViewById(R.id.tv_username_top);

        tvPosts = view.findViewById(R.id.tv_posts);
        tvFollowers = view.findViewById(R.id.tv_followers);
        tvFollowing = view.findViewById(R.id.tv_following);

        Button btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnShareProfile = view.findViewById(R.id.btn_share_profile);
        llHighlight = view.findViewById(R.id.ll_highlight);
        ivProfileMain = view.findViewById(R.id.iv_profile_main);
        ivPost1 = view.findViewById(R.id.iv_post_1);
        ivPost2 = view.findViewById(R.id.iv_post_2);
        ivPost3 = view.findViewById(R.id.iv_post_3);

        tvName.setText(userChandra.getNama());
        tvBio.setText(userChandra.getBio());
        tvUsernameTop.setText(userChandra.getUsername());
        
        if (currentProfileImageUri != null) {
            ivProfileMain.setImageURI(currentProfileImageUri);
        } else {
            ivProfileMain.setImageResource(userChandra.getFotoResId());
        }

        if (tvPosts != null) tvPosts.setText(String.valueOf(userChandra.getPosts()));
        if (tvFollowers != null) tvFollowers.setText(String.valueOf(userChandra.getFollowers()));
        if (tvFollowing != null) tvFollowing.setText(String.valueOf(userChandra.getFollowing()));

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), NextActivity.class);
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

        // Share Profile Button
        btnShareProfile.setOnClickListener(v -> {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Lihat profil Instagram saya: @" + userChandra.getUsername());
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent, "Bagikan Profil Ke"));
        });

        // Highlight Click
        if (llHighlight != null) {
            llHighlight.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Membuka Highlight Story...", Toast.LENGTH_SHORT).show();
            });
        }

        setupPostClick(ivPost1, R.drawable.foto2, "Mengabadikan sebuah moment itu penting, kenapa? karena di setiap potretan ada kenangan yang tidak bisa kita ulang kembali.");
        setupPostClick(ivPost2, R.drawable.foto3, "Kesempatan tidak datang dua kali.");
        setupPostClick(ivPost3, R.drawable.foto4, "Profil saya.");
    }

    private void setupPostClick(ImageView iv, int postImageResId, String caption) {
        if (iv == null) return;
        iv.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), PostDetailActivity.class);
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
