package com.example.trainandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class FilterActivity extends AppCompatActivity {

    private ImageView imageView;
    private Uri imageUri;
    private ImageView idIV1, idIV2, idIV3, idIV4;
    private Bitmap bit1, bit2, bit3, bit4;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        imageView = findViewById(R.id.idIVOriginalImage);
        idIV1 = findViewById(R.id.idIVOne);
        idIV2 = findViewById(R.id.idIVTwo);
        idIV3 = findViewById(R.id.idIVThree);
        idIV4 = findViewById(R.id.idIVFour);
        progressBar = findViewById(R.id.progressBar);

        if (getIntent() != null && getIntent().getStringExtra("imageUri") != null) {
            imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
            displayImage(imageUri);
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            idIV1.setImageBitmap(bitmap);
            idIV2.setImageBitmap(bitmap);
            idIV3.setImageBitmap(bitmap);
            idIV4.setImageBitmap(bitmap);

            new ApplyFiltersTask().execute(bitmap);
        }

        initializeOnClickListeners();
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

    private void setupActionButtons() {
        Button backButton = findViewById(R.id.backButton);
        Button saveButton = findViewById(R.id.saveButton);

        backButton.setOnClickListener(v -> finish());

        saveButton.setOnClickListener(v -> saveImage());
    }

    private void saveImage() {
        if (imageView != null) {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            Uri uri = Uri.parse(path);

            // Display a message to the user
            Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeOnClickListeners() {
        idIV1.setOnClickListener(v -> setupFilterButtons(bit1));
        idIV2.setOnClickListener(v -> setupFilterButtons(bit2));
        idIV3.setOnClickListener(v -> setupFilterButtons(bit3));
        idIV4.setOnClickListener(v -> setupFilterButtons(bit4));
    }

    private void setupFilterButtons(Bitmap bit) {
        imageView.setImageBitmap(bit);
    }

    private class ApplyFiltersTask extends AsyncTask<Bitmap, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            Bitmap bitmap = bitmaps[0];

            bit1 = ImageProcessor.applyGrayScale(bitmap);
            bit2 = ImageProcessor.applyGaussianBlur(bitmap);
            bit3 = ImageProcessor.tintImage(bitmap, 90);
            bit4 = ImageProcessor.applyFleaEffect(bitmap);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            idIV1.setImageBitmap(bit1);
            idIV2.setImageBitmap(bit2);
            idIV3.setImageBitmap(bit3);
            idIV4.setImageBitmap(bit4);
            progressBar.setVisibility(View.GONE);
        }
    }
}
