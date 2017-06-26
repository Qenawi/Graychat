package com.example.qenawi.am.alertdialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.qenawi.am.Interface.custoomDinterface;
import com.example.qenawi.am.R;

/**
 * Created by QEnawi on 4/26/2017.
 */

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener
{

    public Activity c;
    public ImageView save, set,d;
    String img1,Imgcamo;
    custoomDinterface mCallback;

    public CustomDialogClass(Activity a,String img,String Imgcamo,custoomDinterface mCallback)
    {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.img1=img;
        this.Imgcamo=Imgcamo;
        this.mCallback=mCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wallpaperfullscreen);
        save = (ImageView) findViewById(R.id.save);
        set = (ImageView) findViewById(R.id.set);
        d = (ImageView) findViewById(R.id.over_flow_img);
        save.setOnClickListener(this);
        set.setOnClickListener(this);

        Glide.with(c)
                .load(Imgcamo)
                .asBitmap()
                .centerCrop()
                .into(new SimpleTarget<Bitmap>()
                {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        // Do something with bitmap here.
                        d.setImageBitmap(bitmap);
                        Glide.with(c)
                                .load(img1)
                                .asBitmap()
                                .centerCrop()
                                .into(new SimpleTarget<Bitmap>()
                                {
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                        // Do something with bitmap here.
                                        d.setImageBitmap(bitmap);
                                    }
                                });
                    }
                });
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.save:
                mCallback.DoAction(img1,"save");
                break;
            case R.id.set:
                mCallback.DoAction(img1,"set");
                break;
            default:
                break;
        }
        dismiss();
    }
}