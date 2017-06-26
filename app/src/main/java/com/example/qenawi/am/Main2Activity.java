package com.example.qenawi.am;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.qenawi.am.SplashWelcome.ChattScreen;

public class Main2Activity extends AppCompatActivity {

    private  Button btn1;
    private  Button btn2;
    private  Button btn3;
    ImageView v1 ,v2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btn1=(Button)findViewById(R.id.button2); //chat
        btn2=(Button)findViewById(R.id.button3);  // wall paper
        btn3=(Button)findViewById(R.id.button4);  // status
        v1=(ImageView)findViewById(R.id.imageView);
        v2=(ImageView)findViewById(R.id.imageView2);
        btn1.setOnClickListener(
                new View.OnClickListener()
                {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Main2Activity.this, ChattScreen.class);
                startActivity(i);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Main2Activity.this, MainActivity.class);
                i.putExtra("xcx","wall");
                startActivity(i);

            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Main2Activity.this, MainActivity.class);
                i.putExtra("xcx","stat");
                startActivity(i);
            }
        });
       String x= "https://dw9to29mmj727.cloudfront.net/misc/newsletter-naruto3.png";
       loadImageSimpleTarget(x);

    }

    private SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>()
    {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation)
        {
            // do something with the bitmap
            // for demonstration purposes, let's just set it to an ImageView
            v1.setImageBitmap(bitmap);
            Bitmap  bitma=  getResizedBitmap(bitmap,18,18);
            v2.setImageBitmap(bitma);

        }
    };
    private void loadImageSimpleTarget(String Target)
    {
        Glide
                .with(getApplicationContext())
                .load(Target)
                .asBitmap().into(target);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap
                (
                bm, 0, 0, width, height, matrix, false);
     //   bm.recycle();
        return resizedBitmap;
    }
}
