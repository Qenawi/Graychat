package com.example.qenawi.am.Services;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.IOException;

/**
 * Created by QEnawi on 4/5/2017.
 */

public class ChangeWallPaper extends MyBaseTaskService
{
    //Objectat
    public static final String EXTRA_FILE_URI = "extra_file_uri";
    @Override
    public void onCreate()
    {
        super.onCreate();
        //in4lize objects
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        String url=intent.getStringExtra(EXTRA_FILE_URI);
        loadImageSimpleTarget(url);
        return  START_REDELIVER_INTENT;//
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        //enible pubblechatme to bind activity to the service
        // it enable activity to have adirect acess to the service
        return null;
    }
  //------Functions
    private  void SetbG(Bitmap bitmap)
    {
        Toast.makeText(getApplicationContext(),"SET BG",Toast.LENGTH_SHORT).show();
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this.getApplicationContext());
        try
        {

            wallpaperManager.setBitmap(bitmap);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_SHORT).show();
         taskCompleted();
    }
    private SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>()
    {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation)
        {
            // do something with the bitmap
            // for demonstration purposes, let's just set it to an ImageView
            SetbG(bitmap);
        }
    };
    private void loadImageSimpleTarget(String Target)
    {
        Glide
                .with(getApplicationContext())
                .load(Target)
                .asBitmap().into(target);
        taskStarted();
    }
}
