package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;

public class PrintableLine {
    public static final int POSITION_LEFT = 1;
    public static final int POSITION_CENTER = 2;
    public static final int POSITION_RIGHT = 3;
    public static final int DEFAULT_PADDING = 3;
    public int fontSize = 20;
    public Typeface typeFace = null;
    public Bitmap bitmap = null;
    public int bitmapPosition = 0;
    public String imageFromAssets = null;
    public boolean separator = false;
    public boolean bold = false;
    public String left = null;
    public String center = null;
    public String right = null;
    public int padLeft = DEFAULT_PADDING;
    public int padRight = DEFAULT_PADDING;
    public int padUp = DEFAULT_PADDING;
    public int padDown = DEFAULT_PADDING;
    public int rewind = 0;
    public int separatorThickness = 2;
    public boolean border = false;
    public int borderSize = 0;

    public PrintableLine(Bitmap bitmap, int bitmapPosition) {
        this.bitmap = bitmap;
        this.bitmapPosition = bitmapPosition;
    }

    public PrintableLine(String left, String center, String right) {
        this.left = left;
        this.center = center;
        this.right = right;
    }

    public PrintableLine() {
        this.separator = true;
    }

    public PrintableLine(int dividerThick, int dividerPad) {
        this.separator = true;
        this.padLeft = dividerPad;
        this.padRight = dividerPad;
        this.padUp = dividerPad;
        this.padDown = dividerPad;
        this.separatorThickness = dividerThick;
    }

    public PrintableLine(int dividerThick, int dividerPadHorizontal, int dividerPadVertical) {
        this.separator = true;
        this.padLeft = dividerPadHorizontal;
        this.padRight = dividerPadHorizontal;
        this.padUp = dividerPadVertical;
        this.padDown = dividerPadVertical;
        this.separatorThickness = dividerThick;
    }

    public PrintableLine(int rewind) {
        this.rewind = rewind;
        this.padLeft = 0;
        this.padRight = 0;
        this.padUp = 0;
        this.padDown = 0;
    }

    public PrintableLine(String imageFromAssets) {
        this.imageFromAssets = imageFromAssets;
    }

    public PrintableLine setBold() {
        this.bold = true;
        return this;
    }

    public PrintableLine setBorder(int size) {
        this.border = true;
        this.borderSize = size;
        return this;
    }

    public PrintableLine setFont(Typeface typeFace) {
        this.typeFace = typeFace;
        return this;
    }

    public PrintableLine setFontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public PrintableLine setFontSize(int fontSize, Typeface typeFace) {
        this.fontSize = fontSize;
        this.typeFace = typeFace;
        return this;
    }

    public PrintableLine setPadding(int padding) {
        this.padLeft = padding;
        this.padRight = padding;
        this.padUp = padding;
        this.padDown = padding;
        return this;
    }

    public PrintableLine setPadding(int horizontal, int vertical) {
        this.padLeft = horizontal;
        this.padRight = horizontal;
        this.padUp = vertical;
        this.padDown = vertical;
        return this;
    }

    public PrintableLine setPadding(int padLeft, int padRight, int padUp, int padDown) {
        this.padLeft = padLeft;
        this.padRight = padRight;
        this.padUp = padUp;
        this.padDown = padDown;
        return this;
    }

    public PrintableLine bitmapResizeHight(int newHight) {
        int height = bitmap.getHeight();
        float scale = ((float) newHight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), height, matrix, false);
        bitmap.recycle();
        bitmap = resizedBitmap;
        return this;
    }
}
