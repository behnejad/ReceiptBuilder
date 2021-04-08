package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(new ReceiptBuilder(this)
                .addLine(new PrintableLine().setPadding(30, 20))
                .addLine(new PrintableLine(null, "Center text", null))
                .addLine(new PrintableLine("AAA", "مارمالاد", "888B").setFontSize(30).setBorder(3).setPadding(10, 5))
                .addLine(new PrintableLine(1, 50, 10))
                .addLine(new PrintableLine("ABCD", "EFG", "HIJK").setPadding(6))
                .addLine(new PrintableLine())
                .getBitmap());

        setContentView(imageView);
    }
}