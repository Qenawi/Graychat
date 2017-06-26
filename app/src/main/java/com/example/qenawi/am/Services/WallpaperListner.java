package com.example.qenawi.am.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.qenawi.am.Contract.ContractAcc;
import com.example.qenawi.am.Contract.ContractDepug;
import com.example.qenawi.am.Main2Activity;
import com.example.qenawi.am.R;
import com.example.qenawi.am.modles.WallPaberv;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by QEnawi on 4/16/2017.
 */

public class WallpaperListner extends MyBaseTaskService
{
    ;
    @Override
    public IBinder onBind(Intent intent)
    {

        return null;
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        //in4lize objects
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
       // Toast.makeText(getApplicationContext(),"ONSTART",Toast.LENGTH_SHORT).show();
     // String url=intent.getStringExtra(EXTRA_FILE_URI);// get data from intent
        taskStarted();
        checkbg();
        return  START_STICKY;//
    }
    private void checkbg()
    {
        ContractAcc acc=new ContractAcc();
        final String email2= acc.get_username().getEmail();
        DatabaseReference dp_ref = FirebaseDatabase.getInstance().getReference().child("wallpaber");
        Query query1 = dp_ref.orderByChild("email").equalTo(email2).limitToFirst(1);
        query1.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                WallPaberv f;
                Log.v(ContractDepug.PUBTAG,"income data");
                    f = dataSnapshot.getValue(WallPaberv.class);
                    setBg(f.getLink());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }//check_if_chat_room_exist
    private void setBg(String url)
    {

                getApplicationContext().startService(new Intent(getApplicationContext(), ChangeWallPaper.class)
                .putExtra(ChangeWallPaper.EXTRA_FILE_URI, url)
                .setAction("ACTION"));
                  Notift();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
//-----------------------------
    void Notift()
    {
        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext());

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.secondryicon)
                .setTicker("Hearty365")
                .setContentTitle("Upload notification")
                .setContentText("el BG bta3atak et8yrt")
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("O_O");

        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }
}
