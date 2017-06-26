package com.example.qenawi.am.Services;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.qenawi.am.Contract.ContractDepug;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Created by QEnawi on 4/26/2017.
 */

public class save_mg_serv extends MyBaseTaskService {
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.v(ContractDepug.PUBTAG,"start");
        String url=intent.getStringExtra("ZAMPACTO");
        save_img_st(url);
        return  START_REDELIVER_INTENT;//
    }
void save_img_st(String uri)
{
    Bitmap bitmap= null;
    Glide
            .with(getApplicationContext())
            .load(uri)
            .asBitmap().into(target);
    taskStarted();
}

    private SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>()
    {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation)
        {
            // do something with the bitmap
            // for demonstration purposes, let's just set it to an ImageView
            Log.v(ContractDepug.PUBTAG,"img source ready");
            SaveImage(bitmap);
        }
    };
    private void SaveImage(Bitmap finalBitmap)
    {
        Log.v(ContractDepug.PUBTAG,"img source ready");
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Gchatt/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()){       Log.v(ContractDepug.PUBTAG,"file oready exist"); file.delete ();}
        try {
            Log.v(ContractDepug.PUBTAG,"try");
            FileOutputStream out = new FileOutputStream(file);
           finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MediaScannerConnection.scanFile(this,
                new String[] { myDir.getAbsolutePath() }, null,
                new MediaScannerConnection.OnScanCompletedListener()
                {
                    public void onScanCompleted(String path, Uri uri)
                    {
                        //now visible in gallery
                        taskCompleted();
                    }
                }
        );


    }


}
