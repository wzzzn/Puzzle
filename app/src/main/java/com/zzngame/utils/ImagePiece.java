package com.zzngame.utils;

import android.graphics.Bitmap;

/**
 * Created by Host on 2015/2/16.
 */
public class ImagePiece {
    private int index;
    private Bitmap bitmap;

    public ImagePiece() {
        super();
    }

    public ImagePiece(int index, Bitmap bitmap) {
        super();
        this.index = index;
        this.bitmap = bitmap;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "ImagePiece [index=" + index + ", bitmap=" + bitmap + "]";
    }
}
