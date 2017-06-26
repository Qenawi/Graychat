package com.example.qenawi.am.SplashWelcome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qenawi.am.Contract.ContractAcc;
import com.example.qenawi.am.Contract.ContractDepug;
import com.example.qenawi.am.Interface.FireBaseListener;
import com.example.qenawi.am.R;
import com.example.qenawi.am.Services.uBloodWallpaber;
import com.example.qenawi.am.adapters.AdapterMultiView;
import com.example.qenawi.am.modles.MyPair;
import com.example.qenawi.am.modles.smsitem;
import com.example.qenawi.am.modles.users_data_modle;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import br.com.instachat.emojilibrary.controller.WhatsAppPanel;
import br.com.instachat.emojilibrary.model.layout.EmojiCompatActivity;
import br.com.instachat.emojilibrary.model.layout.WhatsAppPanelEventListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChattScreen extends EmojiCompatActivity implements FireBaseListener,WhatsAppPanelEventListener {
    private FireBaseListener mCallback;
    private ArrayList<smsitem> data;
    private RecyclerView rv;
    private AdapterMultiView adapter;
    private String RoomKey;
    private int children_cnt = 0;
    private int Min;    // opjects
    private WhatsAppPanel mBottomPanel;
    private  CircleImageView img;
    private TextView status;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //inis4
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welocome);
        customActionBar("");
        ubdateMyStatus("Online");
       mBottomPanel=new WhatsAppPanel(this,this,R.color.colorPrimary);
        data = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.chat);
        adapter = new AdapterMultiView(data, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        mCallback = this;
        HockUpstatusListner();
        get_Roomkey();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.chat_room, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.add_photo:
                UploodBG();
                break;

            case R.id.wake_up :
                wake_up();
                break;

        }
        return true;
    }
void wake_up()
{
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+12:00"));
    Date currentLocalTime = cal.getTime();
    ContractAcc acc = new ContractAcc();
    DateFormat date2 = new SimpleDateFormat("HH:mm a");
    String localTime2 = date2.format(currentLocalTime);
    final FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    final DatabaseReference fdbr2 = fdb.getReference().child("NOTF/PairChat").child(RoomKey);
    fdbr2.setValue(new smsitem("BIMBO","camo",localTime2, acc.get_username().getEmail()));
}
    void sendmsg(String msg)
    {
        ContractAcc acc = new ContractAcc();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+12:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("yy:MM:dd:HH:mm:ss a");
        DateFormat date2 = new SimpleDateFormat("HH:mm a");
        DateFormat date3 = new SimpleDateFormat("MM:dd");
// you can get seconds by adding  "...:ss" to it
        String localTime = date.format(currentLocalTime);
        String localTime2 = date2.format(currentLocalTime);
        String localTime3 = date3.format(currentLocalTime);
        final FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        final DatabaseReference fdbr = fdb.getReference().child("chatrooms").child(RoomKey).child(localTime3);
        final DatabaseReference fdbr2 = fdb.getReference().child("NOTF/PairChat").child(RoomKey);
        String pushKey = fdbr.push().getKey();
        fdbr.child(pushKey).setValue(new smsitem(msg,"camo",localTime, acc.get_username().getEmail()));
        fdbr2.setValue(new smsitem(msg,"camo",localTime2, acc.get_username().getEmail()));
    }

    void get_Roomkey() {
        ContractAcc con = new ContractAcc();
        users_data_modle mUdm = con.get_username();
        FirebaseDatabase Fdb = FirebaseDatabase.getInstance();
        DatabaseReference Fdbr = Fdb.getReference().child("MyPair");
        Query query = Fdbr.orderByChild("emailBase").equalTo(mUdm.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MyPair myPair = dataSnapshot.getChildren().iterator().next().getValue(MyPair.class);
                mCallback.onDone(myPair.getSecret(), getString(R.string.getImgUri03));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void get_img_cnt() {

        final FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        final DatabaseReference fdbr = fdb.getReference().child("chatrooms").child(RoomKey);
        fdbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                int cnt = (int) dataSnapshot.getChildrenCount();
                Log.v(ContractDepug.PUBTAG, dataSnapshot.getKey());
                mCallback.onDone(cnt, getString(R.string.getImgUri04));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void addMsg(int curnt, int totalcnt, smsitem income) {
        data.add(income);
        if (curnt >= totalcnt) {
            mCallback.onDone(null, getString(R.string.getImgUri05));
        }

    }

    void ftch_msgs(final int chldcnt)
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+12:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date3 = new SimpleDateFormat("MM:dd");
        String localTime3 = date3.format(currentLocalTime);
        int LimitTo;
        LimitTo = 20;
        Min = chldcnt;
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference fdbr = fdb.getReference().child("chatrooms").child(RoomKey).child(localTime3);
        Query query = fdbr.orderByChild("time").limitToLast(LimitTo);
        if (LimitTo < chldcnt)
        {
            Min = LimitTo;
        }
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                children_cnt++;
                addMsg(children_cnt, Min, dataSnapshot.getValue(smsitem.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
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
    }
//------------------------------------------------------------
@Override
public void onSendClicked()
{

    sendmsg(mBottomPanel.getText());
    mBottomPanel.setText("");
}
    //-------------------------------------------------------------------------------------
    private void UploodBG() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Integer.valueOf(getString(R.string.selectPhotoIntent)));
    }

    private void UploodBGHelper(Uri uri)
    {
        // start uplood service
        getApplicationContext().startService(new Intent(getApplicationContext(), uBloodWallpaber.class)
                .putExtra(getString(R.string.uBloodwallpaperServiceintent), uri.toString())
                .putExtra(getString(R.string.uBloodwallpaperServiceintentFlag), 1)
                .putExtra("Roomkey", RoomKey)
                .setAction("ACTION"));
    }

    //---------------------------------------------------------------------------------------
    @Override
    public void onDone(Object O, String TAG)
    {
        if (TAG.equals(getString(R.string.getImgUri03))) {
            // key is ready start fetchng data
            RoomKey = (String) O;
            get_img_cnt();
        } else if (TAG.equals(getString(R.string.getImgUri04)))
        {// key is ready start fetchng data
            ftch_msgs((int) O);
        }
        else if (TAG.equals(getString(R.string.getImgUri05)))
        {
            // key is ready start fetchng data
            adapter.notifyDataSetChanged();
            adapter.setSelectedItem(adapter.getItemCount() - 1);
            rv.scrollToPosition(adapter.getItemCount() - 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Integer.valueOf(getString(R.string.selectPhotoIntent))) {
            if (resultCode == RESULT_OK) {
                Uri Imguri = data.getData();
                UploodBGHelper(Imguri);

            } else {
                Toast.makeText(this, "Faild to get img", Toast.LENGTH_SHORT).show();
            }

        }

    }

    //-------------------------------------------------------------------------------
    private void customActionBar(String statusx)
    {
//Get the default actionbar instance
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        if (mActionBar==null){Log.v(ContractDepug.PUBTAG,"null instance");return;}
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
//Initializes the custom action bar layout
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.chatroomheader, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
//Detect the button click event of the home button in the actionbar
         img = (CircleImageView) findViewById(R.id.personimg);
         status=(TextView)findViewById(R.id.chatroomstatus);
        status.setText(statusx);
    } // action Bar
    void HockUpstatusListner()
    {
        String em= getStoredPair();
        String child_n=em.substring(0,em.indexOf("@"));
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference fdbr = fdb.getReference().child("online_Seen").child(child_n);
        fdbr.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.v(ContractDepug.PUBTAG,dataSnapshot.getKey()+" key status");
                String datae=dataSnapshot.getValue(String.class);
                change_status(null,datae);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }
    void change_status(@Nullable String img_uri,String status)
    {
        customActionBar(status);
    }
    void  ubdateMyStatus( String Status)
    {
        String child_n=new ContractAcc().get_username().getEmail();
        String mail_head=child_n.substring(0,child_n.indexOf("@"));
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference fdbr = fdb.getReference().child("online_Seen").child(mail_head);
        fdbr.setValue((String)Status).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {

            }
        });
    }
    String getStoredPair()
    {
      return   PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.getPairPref),"null");
    }

    @Override
    protected void onPause()
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+12:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date3 = new SimpleDateFormat("dd:HH:mm a");
        String d=date3.format(currentLocalTime);
         ubdateMyStatus("Seen"+d );
        super.onPause();
    }
}
