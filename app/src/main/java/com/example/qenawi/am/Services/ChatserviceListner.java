package com.example.qenawi.am.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.example.qenawi.am.Contract.ContractAcc;
import com.example.qenawi.am.Main3Activity;
import com.example.qenawi.am.R;
import com.example.qenawi.am.SplashWelcome.ChattScreen;
import com.example.qenawi.am.modles.smsitem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by QEnawi on 4/18/2017.
 */

public class ChatserviceListner extends MyBaseTaskService
{
    @Nullable
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
        String key=PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.getPairPrefKEY),"null");
        checkchat(key);
        return  START_STICKY;//
    }
    private void checkchat(String RoomKey)
    {
        ContractAcc acc=new ContractAcc();
        final String email2= acc.get_username().getEmail();
        final FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference fdbr = fdb.getReference().child("NOTF").child("PairChat").child(RoomKey).getParent();
        Query query=fdbr;
        query.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                ContractAcc acc=new ContractAcc();
                final String email2= acc.get_username().getEmail();
                smsitem fg=dataSnapshot.getValue(smsitem.class);
                if (!email2.equals(fg.getSender()))
                {
                    Notift(fg.getMsg(),fg.getTime());
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }//check_if_chat_room_exist

    void Notift(String data ,String data2)
    {
        if (data.equals("BIMBO"))
        {
            Notift1();
        }
        else
        {
            Notift0(data,data2);
        }
    }
    void Notift0(String data ,String data2)
    {
        Intent intent = new Intent(getApplicationContext(), ChattScreen.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext());

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.secondryicon)
                .setTicker("xXx")
                .setContentTitle("Y S Panda")
                .setContentText(data)
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("🐼 "+data2);
        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }
    void Notift1()
    {
        Intent dialogIntent = new Intent(this, Main3Activity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
    }

    @Override
    public void onDestroy()
    {
        Toast.makeText(getApplicationContext(),"serv destroied",Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

}
