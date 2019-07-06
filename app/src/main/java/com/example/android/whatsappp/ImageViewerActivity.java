package com.example.android.whatsappp;

import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class ImageViewerActivity extends AppCompatActivity {

    private ImageView imageView;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); */

        imageView = findViewById(R.id.imageViewer);
        imageUrl = getIntent().getStringExtra("url");

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(10);
        circularProgressDrawable.setCenterRadius(60);
        circularProgressDrawable.start() ;

        Glide.with(ImageViewerActivity.this).load(imageUrl)
                .placeholder(circularProgressDrawable).into(imageView);


    }
}
