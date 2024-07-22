package com.example.trainandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class FilterActivity extends AppCompatActivity {

    private ImageView imageView;
    private Uri imageUri;
    private Bitmap editedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        imageView = findViewById(R.id.idIVOriginalImage);

        if (getIntent() != null && getIntent().getStringExtra("imageUri") != null) {
            imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
            displayImage(imageUri);
        }

        setupFilterButtons();
        setupActionButtons();
    }

    private void displayImage(Uri uri) {
        try {
            InputStream imageStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupFilterButtons() {
        findViewById(R.id.idLLVignette).setOnClickListener(v -> applyGrayScaleFilter());
    }

    private void applyGrayScaleFilter() {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        editedBitmap = ImageProcessor.applyGrayScale(bitmap);
        imageView.setImageBitmap(editedBitmap);
    }

    private void setupActionButtons() {
        Button backButton = findViewById(R.id.backButton);
        Button saveButton = findViewById(R.id.saveButton);

        backButton.setOnClickListener(v -> finish());

        saveButton.setOnClickListener(v -> saveImage());
    }

    private void saveImage() {
        if (editedBitmap != null) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            editedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), editedBitmap, "Title", null);
            Uri uri = Uri.parse(path);

            // Hiển thị thông báo cho người dùng
            Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No image to save", Toast.LENGTH_SHORT).show();
        }
    }
}
