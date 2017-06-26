package com.example.qenawi.am.Services;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import com.example.qenawi.am.Contract.ContractAcc;
import com.example.qenawi.am.Contract.ContractDepug;
import com.example.qenawi.am.Interface.ubloodservice;
import com.example.qenawi.am.R;
import com.example.qenawi.am.modles.StorageWallpaber;
import com.example.qenawi.am.modles.smsitem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by QEnawi on 4/13/2017.
 */

public class uBloodWallpaber extends MyBaseTaskService implements ubloodservice
{
    //
    //---------- progress par
    private NotificationManager mNotifyManager;
    private Builder build;
    int id = 1;
    //---------------------------------
    private  int flag;
    private  String dwnlnk,dwnlnk_camo;
    private Uri uri_string,uri_string_camo;
    private ubloodservice mlisttner=this;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        taskStarted();
        //------------------
        // show load manger
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
           build = new NotificationCompat.Builder(this);
           build.setContentTitle("Upload")
               .setContentText("Upload in progress")
               .setSmallIcon(R.drawable.pandaublood);
 // Displays the progress bar for the first time.
        build.setProgress(0, 0, true);
         mNotifyManager.notify(id, build.build());
        //---------------------------------------------------------
        flag=0;
        uri_string=Uri.parse(intent.getStringExtra(getString(R.string.uBloodwallpaperServiceintent)));
        flag=intent.getIntExtra(getString(R.string.uBloodwallpaperServiceintentFlag),0);
       final String key = intent.getStringExtra("Roomkey");
        //----------Basic steps---------------//
        ublood_camo(uri_string.toString(), key); //genirate bit map   camo and ublood it
        return START_REDELIVER_INTENT;//

    }
    private void ublood_camo(String uri,String key)
    {
        final Uri imageUri=Uri.parse(uri);
        Bitmap bitmap = null;
        try
        {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Bitmap low_lvl = getResizedBitmap(bitmap, 16, 16);
        //get uri
        Uri urilow=getImageUri(this,low_lvl);
        uri_string_camo=urilow;
        mlisttner.onComplete(uri_string_camo.toString(),key,"0");//0phoyoa  compressed
    }
    private void Ublood(final String urifile, final String KEY,final  String tokken )// t 00 11
    {
        final Uri file=Uri.parse(urifile);
        final ArrayList<String>O=new ArrayList<>();
        Log.v(ContractDepug.PUBTAG, file.getAuthority());
        FirebaseStorage storageRef = FirebaseStorage.getInstance();
        StorageReference riversRef = storageRef.getReference().child("wallbapers/" + file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);
// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                String ec=taskSnapshot.getDownloadUrl().toString();// dwn ln
                if (tokken.equals("00")) // 0-> storge uri lnk 1->fire base dowenlood link
                {
                 dwnlnk_camo=ec;
                }
                else {dwnlnk=ec;}
     mlisttner.onComplete(urifile,KEY,tokken);//file downlood link, key to add to dp , token to filter call back
            }
        }
        ).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Log.v(ContractDepug.PUBTAG, "Ublood Faild");
                taskCompleted();
                // hide
            }
        });
    }
    // - add data to dp
    void addToDp()
    {
        String result,result2;
        result=uri_string.getLastPathSegment();
        result2=uri_string_camo.getLastPathSegment();
        if (result.equals("")||result2.equals("")){return;}
        FirebaseDatabase fdb=FirebaseDatabase.getInstance();
        DatabaseReference Fdbr=fdb.getReference().child("Storage").child("wallbaber");//. key
        String key=Fdbr.push().getKey();
        Fdbr.child(key).setValue(new StorageWallpaber(result,result2)).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
            //    Toast.makeText(getApplication(),"Ublood Sucsess",Toast.LENGTH_SHORT).show();
                // Removes the progress bar
                build.setContentText("ubload Complete").setProgress(0, 0, false);
                mNotifyManager.notify(id, build.build());
                  taskCompleted();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
          //      Toast.makeText(getApplication(),"Ublood Faild",Toast.LENGTH_SHORT).show();
                  taskCompleted();
            }
        });
    }
    void addToDp2(String key)
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+12:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date3 = new SimpleDateFormat("MM:dd");
// you can get seconds by adding  "...:ss" to it
        String localTime0 = date3.format(currentLocalTime);
        ContractAcc acc=new ContractAcc();
        DateFormat date = new SimpleDateFormat("yy:MM:dd:HH:mm:ss a");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
        String localTime = date.format(currentLocalTime);
        final FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        final DatabaseReference fdbr = fdb.getReference().child("chatrooms").child(key).child(localTime0);
        String pushKey=fdbr.push().getKey();
        fdbr.child(pushKey).setValue(new smsitem(dwnlnk,dwnlnk_camo,localTime,acc.get_username().getEmail())).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                build.setContentText("ubload Complete").setProgress(0, 0, false);
                mNotifyManager.notify(id, build.build());
                taskCompleted();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                taskCompleted();

            }
        });
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
    public Uri getImageUri(Context inContext, Bitmap inImage)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    @Override
    public void onComplete(Object O, Object O2,String Tag)
    {
       if (Tag.equals("0")) // compress done call ublood to ublood camo
       {
           Log.v(ContractDepug.PUBTAG,"x-> start ublodding camo");
           Ublood((String) O,(String) O2,"00");
       }
       else if (Tag.equals("00")) // ublodding  camo is done delete 2
        {
            Log.v(ContractDepug.PUBTAG,"x-> ublod done start delete camo");
            delete_element((String)O,(String)O2); // delte
        }
       else if (Tag.equals("1")) // delete is done ublood baase
       {
           Log.v(ContractDepug.PUBTAG,"x-> delete done start ublood base");
             Ublood(uri_string.toString(),(String) O2,"11");
       }
       else if(Tag.equals("11")) // all photos uploded
       {
               if (flag==1)
               {
                   addToDp2((String)O2);
               }
               else {addToDp();}
       }
    }
    void delete_element(String uri,String key)
    {
        Uri d=Uri.parse(uri);
        try
        {
            getContentResolver().delete(d,null,null);
        }catch
                ( Exception e){e.printStackTrace();}

         //case erorr oR extra Boom
        // sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        mlisttner.onComplete(null,key,"1");
    }
 //---------------------------------------------------------------------
// steps
// 1 creat  small and get url
// 2 ublood small and on finish start ublooding big
//on finish ublooding big add 2 photos to dp
}