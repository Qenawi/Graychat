package com.example.qenawi.am;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class Main3Activity extends AppCompatActivity
{
    Button x;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        x=(Button)findViewById(R.id.KK);
        Uri r1= RingtoneManager.getActualDefaultRingtoneUri(this.getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
            finish();
            }
        });
        lac(r1);
    }
    void lac(Uri uri)
    {
        MediaPlayer mPlayer=new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try
        {
            mPlayer.setDataSource(getApplicationContext(), uri);
            mPlayer.prepare();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        mPlayer.start();
        mPlayer.setLooping(true);
    }
}
