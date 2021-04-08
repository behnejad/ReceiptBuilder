package com.example.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ReceiptBuilder {
    private int paperWidth = 384;
    private Context context;
    private ArrayList<PrintableLine> arrayList = new ArrayList<>();
    private Typeface defaultTypeface;

    public ReceiptBuilder(Context context) {
        this.context = context;
    }

    public ReceiptBuilder(Context context, int paperWidth) {
        this.context = context;
        this.paperWidth = paperWidth;
    }

    public ReceiptBuilder(Context context, Typeface defaultTypeface) {
        this.context = context;
        this.defaultTypeface = defaultTypeface;
    }

    public ReceiptBuilder(Context context, int paperWidth, Typeface defaultTypeface) {
        this.context = context;
        this.defaultTypeface = defaultTypeface;
        this.paperWidth = paperWidth;
    }

    public ReceiptBuilder addLine(PrintableLine printableLine) {
        arrayList.add(printableLine);
        return this;
    }

    public Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public Bitmap getBitmap() {
        Rect bounds = new Rect();
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        paint.setAlpha(255);

        int height = 0;
        int linePos = 0;
        int startLinePos = 0;

        for (PrintableLine p : arrayList) {
            height += p.padUp + p.padDown;
            height -= p.rewind;

            if (p.separator) {
                height += p.separatorThickness;
            } else if (p.imageFromAssets != null) {
                p.bitmap = getImageFromAssetsFile(p.imageFromAssets);
                if (p.bitmap != null) {
                    height += p.bitmap.getHeight();
                }
            } else if (p.bitmap != null) {
                height += p.bitmap.getHeight();
            } else if (p.left != null || p.right != null || p.center != null) {
                paint.setTextSize(p.fontSize);
                paint.setFakeBoldText(p.bold);

                if (p.typeFace != null) {
                    paint.setTypeface(p.typeFace);
                } else if (defaultTypeface != null) {
                    paint.setTypeface(defaultTypeface);
                }

                int m = 0;
                if (p.left != null) {
                    paint.getTextBounds(p.left, 0, p.left.length(), bounds);
                    m = Math.max(bounds.height(), m);
                }

                if (p.center != null) {
                    paint.getTextBounds(p.center, 0, p.center.length(), bounds);
                    m = Math.max(bounds.height(), m);
                }

                if (p.right != null) {
                    paint.getTextBounds(p.right, 0, p.right.length(), bounds);
                    m = Math.max(bounds.height(), m);
                }

                height += m;
            }
        }

        Bitmap res = Bitmap.createBitmap(paperWidth, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(res);
//        paint.setColor(Color.WHITE);
//        canvas.drawRect(0, linePos, paperWidth, height, paint);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        for (PrintableLine p : arrayList) {
            linePos -= p.rewind;
            startLinePos = linePos;

            if (p.separator && p.separatorThickness != 0) {
                canvas.drawRect(p.padLeft, linePos + p.padUp + p.borderSize, paperWidth - p.padRight - p.borderSize,
                        linePos + p.padUp + p.borderSize + p.separatorThickness, paint);
                linePos += p.separatorThickness;
            } else if (p.bitmap != null) {
                if (p.bitmapPosition == PrintableLine.POSITION_LEFT) {
                    canvas.drawBitmap(p.bitmap, p.padLeft, linePos + p.padUp, paint);
                } else if (p.bitmapPosition == PrintableLine.POSITION_CENTER) {
                    canvas.drawBitmap(p.bitmap, (float) (paperWidth - p.bitmap.getWidth()) / 2, linePos + p.padUp + p.borderSize, paint);
                } else {
                    canvas.drawBitmap(p.bitmap, paperWidth - p.bitmap.getWidth() - p.borderSize - p.padRight, linePos + p.padUp + p.borderSize, paint);
                }

                linePos += p.bitmap.getHeight();
                p.bitmap.recycle();
            } else if (p.left != null || p.right != null || p.center != null) {
                paint.setTextSize(p.fontSize);
                paint.setFakeBoldText(p.bold);

                if (p.typeFace != null) {
                    paint.setTypeface(p.typeFace);
                } else if (defaultTypeface != null) {
                    paint.setTypeface(defaultTypeface);
                }

                int m = 0;
                if (p.left != null) {
                    paint.getTextBounds(p.left + p.borderSize, 0, p.left.length(), bounds);
                    m = Math.max(bounds.height(), m);
//                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText(p.left, p.padLeft + p.borderSize, linePos + p.padUp + p.borderSize + bounds.height(), paint);
                }

                if (p.center != null) {
                    paint.getTextBounds(p.center, 0, p.center.length(), bounds);
                    m = Math.max(bounds.height(), m);
//                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText(p.center, (float) (paperWidth - bounds.width()) / 2, linePos + p.padUp + p.borderSize + bounds.height(), paint);
                }

                if (p.right != null) {
                    paint.getTextBounds(p.right, 0, p.right.length(), bounds);
                    m = Math.max(bounds.height(), m);
//                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText(p.right, paperWidth - p.padRight - bounds.width() - p.borderSize, linePos + p.padUp + p.borderSize + bounds.height(), paint);
                }

                linePos += m;
            }

            linePos += p.padUp + p.padDown + p.borderSize + p.borderSize;

            if (p.border) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(p.borderSize);
                canvas.drawRect((float) (p.padLeft + p.borderSize) / 2, startLinePos + p.borderSize,
                        paperWidth - (float) (p.padLeft + p.borderSize) / 2, linePos - p.borderSize, paint);
                paint.setStyle(Paint.Style.FILL);
            }
        }

        return res;
    }
}
