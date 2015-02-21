package com.zzngame.utils;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Host on 2015/2/16.
 */
public class ImageSplitterUtil {
    /**
     * @param bitmap 原始图
     * @param xPiece 横向块数
     * @param yPiece 纵向块数
     * @return
     */
    public static List<ImagePiece> splitImage(Bitmap bitmap, int xPiece, int yPiece) {

        List<ImagePiece> imagePieces = new ArrayList<ImagePiece>();

        int _width = bitmap.getWidth();
        int _height = bitmap.getHeight();

        int _xPieceWidth = _width / xPiece;
        int _yPieceWidth = _height / yPiece;

        for (int i = 0; i < yPiece; i++) {
            for (int j = 0; j < xPiece; j++) {

                ImagePiece imagePiece = new ImagePiece();
                imagePiece.setIndex(j + i * xPiece);

                int x = j * _xPieceWidth;
                int y = i * _yPieceWidth;

                imagePiece.setBitmap(Bitmap.createBitmap(bitmap, x, y,
                        _xPieceWidth, _yPieceWidth));

                imagePieces.add(imagePiece);
            }
        }
        return imagePieces;
    }
//    public static List<ImagePiece> splitImage(Bitmap bitmap, int piece) {
//
//        List<ImagePiece> imagePieces = new ArrayList<ImagePiece>();
//
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//
//        int pieceWidth = Math.min(width, height) / piece;
//
//        for (int i = 0; i < piece; i++) {
//            for (int j = 0; j < piece; j++) {
//
//                ImagePiece imagePiece = new ImagePiece();
//                imagePiece.setIndex(j + i * piece);
//
//                int x = j * pieceWidth;
//                int y = i * pieceWidth;
//
//                imagePiece.setBitmap(Bitmap.createBitmap(bitmap, x, y,
//                        pieceWidth, pieceWidth));
//
//                imagePieces.add(imagePiece);
//            }
//        }
//        return imagePieces;
//    }

}
