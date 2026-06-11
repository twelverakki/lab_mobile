package com.example.lab_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditFieldActivity extends AppCompatActivity {

    private EditText etValue;
    private TextView tvTitle, tvLabel, tvHelper, btnDone;
    private ImageView btnCancel;
    private String fieldKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_field);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.header).getParent().getParent() instanceof android.view.View ? (android.view.View) findViewById(R.id.header).getParent() : findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etValue = findViewById(R.id.et_value);
        tvTitle = findViewById(R.id.tv_title);
        tvLabel = findViewById(R.id.tv_label);
        tvHelper = findViewById(R.id.tv_helper);
        btnDone = findViewById(R.id.btn_done);
        btnCancel = findViewById(R.id.btn_cancel);

        // Ambil data dari intent
        fieldKey = getIntent().getStringExtra("field_key");
        String fieldTitle = getIntent().getStringExtra("field_title");
        String fieldValue = getIntent().getStringExtra("field_value");
        String fieldHelper = getIntent().getStringExtra("field_helper");

        tvTitle.setText(fieldTitle);
        tvLabel.setText(fieldTitle);
        etValue.setText(fieldValue);
        if (fieldHelper != null) {
            tvHelper.setText(fieldHelper);
        }

        // Fokus ke EditText dan pindahkan kursor ke akhir
        etValue.requestFocus();
        if (fieldValue != null) {
            etValue.setSelection(fieldValue.length());
        }

        btnDone.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("field_key", fieldKey);
            resultIntent.putExtra("field_value", etValue.getText().toString());
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}
