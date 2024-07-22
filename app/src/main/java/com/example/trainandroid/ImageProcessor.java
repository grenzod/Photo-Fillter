package com.example.trainandroid;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ImageProcessor {

    public static Bitmap applyGrayScale(Bitmap original) {
        int width, height;
        height = original.getHeight();
        width = original.getWidth();

        Bitmap grayScaledBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = original.getPixel(x, y);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);

                // Tính giá trị xám của pixel
                int gray = (red + green + blue) / 3;
                int newPixel = Color.rgb(gray, gray, gray);

                grayScaledBitmap.setPixel(x, y, newPixel);
            }
        }
        return grayScaledBitmap;
    }
}
