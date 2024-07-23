package com.example.trainandroid;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;

import androidx.annotation.ColorInt;

import java.util.Random;


public class ImageProcessor {

    public static Bitmap applyGrayScale(Bitmap original) {
        int width = original.getWidth();
        int height = original.getHeight();
        Bitmap grayScaledBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = original.getPixel(x, y);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);

                int gray = (red + green + blue) / 3;
                int newPixel = Color.rgb(gray, gray, gray);

                grayScaledBitmap.setPixel(x, y, newPixel);
            }
        }

        return grayScaledBitmap;
    }

    public static Bitmap applyFleaEffect(Bitmap originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int[] pixels = new int[width * height];
        originalImage.getPixels(pixels, 0, width, 0, 0, width, height);
        Random random = new Random();
        int index = 0;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                index = y * width + x;
                int randColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                pixels[index] |= randColor;
            }
        }
        Bitmap bmOut = Bitmap.createBitmap(width, height, originalImage.getConfig());
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }

    public static Bitmap tintImage(Bitmap originalImage, int degree) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int[] pix = new int[width * height];
        originalImage.getPixels(pix, 0, width, 0, 0, width, height);
        int RY, GY, BY, RYY, GYY, BYY, R, G, B, Y;
        double angle = (Math.PI * (double) degree) / 180.0;
        int S = (int) (256 * Math.sin(angle));
        int C = (int) (256 * Math.cos(angle));

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                int r = (pix[index] >> 16) & 0xff;
                int g = (pix[index] >> 8) & 0xff;
                int b = pix[index] & 0xff;
                RY = (70 * r - 59 * g - 11 * b) / 100;
                GY = (-30 * r + 41 * g - 11 * b) / 100;
                BY = (-30 * r - 59 * g + 89 * b) / 100;
                Y = (30 * r + 59 * g + 11 * b) / 100;
                RYY = (S * BY + C * RY) / 256;
                BYY = (C * BY - S * RY) / 256;
                GYY = (-51 * RYY - 19 * BYY) / 100;
                R = Y + RYY;
                R = (R < 0) ? 0 : (Math.min(R, 255));
                G = Y + GYY;
                G = (G < 0) ? 0 : (Math.min(G, 255));
                B = Y + BYY;
                B = (B < 0) ? 0 : (Math.min(B, 255));
                pix[index] = 0xff000000 | (R << 16) | (G << 8) | B;
            }
        }
        Bitmap outBitmap = Bitmap.createBitmap(width, height, originalImage.getConfig());
        outBitmap.setPixels(pix, 0, width, 0, 0, width, height);
        return outBitmap;
    }

    public static Bitmap applyGaussianBlur(Bitmap src) {
        double[][] GaussianBlurConfig = new double[][] {
                { 1, 2, 1 },
                { 2, 4, 2 },
                { 1, 2, 1 }
        };
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.applyConfig(GaussianBlurConfig);
        convMatrix.Factor = 16;
        convMatrix.Offset = 0;
        return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
    }
}