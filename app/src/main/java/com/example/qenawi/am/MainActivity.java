package com.example.qenawi.am;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.example.qenawi.am.Contract.ContractAcc;
import com.example.qenawi.am.Contract.ContractDepug;
import com.example.qenawi.am.DataBase.DpHelper;
import com.example.qenawi.am.Fragments.Status;
import com.example.qenawi.am.Fragments.WALLPABER;
import com.example.qenawi.am.Interface.FireBaseListener;
import com.example.qenawi.am.Services.ChatserviceListner;
import com.example.qenawi.am.Services.WallpaperListner;
import com.example.qenawi.am.modles.MyPair;
import com.example.qenawi.am.modles.users_data_modle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements FireBaseListener, Status.OnStatusFragmentInteractionListener, WALLPABER.OnWallPaperFragmentInteractionListener {
    //objects
    private FireBaseListener mCallBack;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //------------------------------------------------------------
      String f=PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.getPairPrefKEY),"null");
        Log.v(ContractDepug.PUBTAG,"key"+getStoredPair());
         f=getStoredPairKey();
        Log.v(ContractDepug.PUBTAG,"key2"+f);
        lizationsini();
        is_user_signed();
        //-<*!*>
    }

    //---Listners
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onwallpaperFragmentInteraction(Uri uri) {

    }

    //-------------calls------------
    private void Call_MAIN() // status
    {
        Status fragment = new Status();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.activity_main_ViewHolder, fragment, "main_frag"); //Container -> R.id.contentFragment
        transaction.commit();
    }

    private void Call_MAIN2()//walll baber
    {
        WALLPABER fragment = new WALLPABER();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.activity_main_ViewHolder, fragment, "main_frag2"); //Container -> R.id.contentFragment
        transaction.commit();
    }

    private void Call_Activity2()
    {
        Intent i = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(i);
    }

    // Functions
    private void is_user_signed()
    {
        ContractAcc con = new ContractAcc();
        users_data_modle mUdm = con.get_username();
        if (mUdm == null)
        {
            // Not signed in, launch the Sign In activity
            startActivityForResult(new Intent(this, SignInActivity.class), 1);
        }
        else
        {
            getPair();
        }

    }

    private void lizationsini()
    {
        mCallBack = this;
        DpHelper dp = new DpHelper(this, "aAndm.db", null, 2);
        dp.check();
    }

    private void getPair()
    {
        if (getStoredPair() != "null")
        {
            mCallBack.onDone(getStoredPair(), getString(R.string.getPair));
            return;
        }
        ContractAcc con = new ContractAcc();
        users_data_modle mUdm = con.get_username();
        String ret = "";
        DatabaseReference dp_ref = FirebaseDatabase.getInstance().getReference().child("MyPair");
        Query query1 = dp_ref.orderByChild("emailBase").equalTo(mUdm.getEmail());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v(ContractDepug.PUBTAG, "getpair1");
                MyPair myPair = null;
                for (DataSnapshot election : dataSnapshot.getChildren()) {
                    myPair = election.getValue(MyPair.class);
                    Log.v(ContractDepug.PUBTAG, myPair.getEmailPair());
                }
                if (myPair != null) {

                    mCallBack.onDone(myPair.getEmailPair(), getString(R.string.getPair));
                } else {
                    pickPair();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //------------------------Listner
    @Override
    public void onDone(Object O, String TAG)
    {
        if (getString(R.string.getPair).equals(TAG))
        {
            setStoredPair((String) O);
            if (!isMyServiceRunning(WallpaperListner.class))
            {
                Start_service();
            }
            getPairKey();

        }
        else if (getString(R.string.getPairPrefKEYIntent).equals(TAG))
        {
            if(!isMyServiceRunning(ChatserviceListner.class))
            {
                Start_service2((String) O);
            }
            try
            {
                String data="K";
                data= getIntent().getStringExtra("xcx");
                switch (data)
                {
                    case "chat":  Call_Activity2(); break;
                    case "wall":        Call_MAIN2(); break;
                    case "stat":         Call_MAIN();  break;
                    default: Call_Activity2(); break;
                }
            }
            catch (Exception e){e.printStackTrace();}
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(ContractDepug.PUBTAG, "res2");
        if (requestCode == 1) {
            Log.v(ContractDepug.PUBTAG, "res1");
            if (resultCode == Activity.RESULT_OK)
            {
                // sign in finished
                getPair();
            }
            if (resultCode == Activity.RESULT_CANCELED)
            {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    //----------------------------------------
    void setStoredPair(String value)
    {
        getPreferences(MODE_PRIVATE).edit().putString(getString(R.string.getPairPref), value).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getString(R.string.getPairPref),value).apply();
    }

    String getStoredPair()
    {

        return getPreferences(MODE_PRIVATE).getString(getString(R.string.getPairPref), "null");
    }

    void pickPair() {
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("pick ur pair")
                .setMessage("enter your pair?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        Log.v(ContractDepug.PUBTAG, task + ">task");
                        //add pair to dp
                        addPair(task);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    void addPair(String emailPair) {
        //add pair-> to mypair and to His pair
        //todo 00200 fix it and make it more strong
        ContractAcc con = new ContractAcc();
        users_data_modle mUdm = con.get_username();
        final DatabaseReference dpf = FirebaseDatabase.getInstance().getReference();//->
        String push_key = dpf.child("MyPair").push().getKey();
        String ChatRoomKey = dpf.child("MyPair").push().getKey();
        dpf.child("MyPair").child(push_key).setValue(new MyPair(mUdm.getEmail(), emailPair, ChatRoomKey));
        push_key = dpf.child("MyPair").push().getKey();// add to other user dp
        dpf.child("MyPair").child(push_key).setValue(new MyPair(emailPair, mUdm.getEmail(), ChatRoomKey));
    }

    //-----------------------service running check------------------------------------------------
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void Start_service()
    {
        startService(new Intent(this, WallpaperListner.class)
                .setAction("ACTION"));
    }
    private void Start_service2(String O)
    {
          startService(new Intent(this, ChatserviceListner.class)
                .setAction("ACTION"));
    }
    void getPairKey()
    {
        String ret=getStoredPairKey();
        if(ret!="null")
        {
            mCallBack.onDone(ret,getString(R.string.getPairPrefKEYIntent));
        }
          get_Roomkey();
    }
    void setStoredPairKey(String value)
    {
        getPreferences(MODE_PRIVATE).edit().putString(getString(R.string.getPairPrefKEY), value).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getString(R.string.getPairPrefKEY),value).apply();
    }

    String getStoredPairKey()
    {
        return getPreferences(MODE_PRIVATE).getString(getString(R.string.getPairPrefKEY), "null");
    }
    void get_Roomkey()
    {

        ContractAcc con = new ContractAcc();
        users_data_modle mUdm = con.get_username();
        FirebaseDatabase Fdb = FirebaseDatabase.getInstance();
        DatabaseReference Fdbr = Fdb.getReference().child("MyPair");
        Query query = Fdbr.orderByChild("emailBase").equalTo(mUdm.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                MyPair myPair = dataSnapshot.getChildren().iterator().next().getValue(MyPair.class);
                setStoredPairKey(myPair.getSecret());
                mCallBack.onDone(myPair.getSecret(), getString(R.string.getPairPrefKEYIntent));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
