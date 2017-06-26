package com.example.qenawi.am.Fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qenawi.am.Contract.ContractAcc;
import com.example.qenawi.am.Contract.ContractDepug;
import com.example.qenawi.am.Main2Activity;
import com.example.qenawi.am.R;
import com.example.qenawi.am.modles.Statusm;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Status extends Fragment implements AdapterView.OnItemSelectedListener {
    //objects
    private OnStatusFragmentInteractionListener mListener;
    private ImageView mStatusImg;
    private String emailPair;


    public Status() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Root;
        //--- inish
        Root = inflater.inflate(R.layout.fragment_status, container, false);
        mStatusImg = (ImageView) Root.findViewById(R.id.fragment_status_GiFStatus);
        emailPair = getStoredPair();

        // Spinner element
        Spinner spinner = (Spinner) Root.findViewById(R.id.spinner);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("watting");
        categories.add("eating");
        categories.add("study");
        categories.add("workout");
        categories.add("cute");
        categories.add("SLEEP");
        categories.add("fe al kolia");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        //------
        get_Status();
        return Root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onAction(Uri uri)
    {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void ubdateUi(String lnk) {
        Log.v(ContractDepug.PUBTAG, lnk + " >< ");
        try {
            ChangeName(lnk);
        }catch (Exception e )
        {
            e.printStackTrace();
        }
        try {
            Glide.with(getActivity()).load(lnk).asGif().fitCenter().crossFade().into(mStatusImg);
        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStatusFragmentInteractionListener) {
            mListener = (OnStatusFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l)
    {
        if (getActivity()==null ){return;}
        String item = parent.getItemAtPosition(i).toString();
        ubdateStatus(item);
        //------------------------------------------------------------------------212
        Intent intent = new Intent(getActivity(), Main2Activity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(getActivity());

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .setTicker("Hearty365")
                .setContentTitle("Default notification")
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //-------------------listner------------------------
    public interface OnStatusFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //-------------------Data-------------------------------
    private void get_Status() {
        DatabaseReference dp_ref = FirebaseDatabase.getInstance().getReference().child("Status");
        Query query1 = dp_ref.orderByChild("email").equalTo(emailPair);
        query1.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Statusm mStatus = null;
                for (DataSnapshot election : dataSnapshot.getChildren())
                {
                    mStatus = election.getValue(Statusm.class);
                 //   Log.v(ContractDepug.PUBTAG,mStatus.getEmail()+" "+mStatus.getStatusGif());
                }
                if (mStatus!= null&&mStatus.getStatusGif() != null)
                {
                    ubdateUi(mStatus.getStatusGif());
                }
                else
                    {
                    Toast.makeText(getActivity(), "u have no pair yet", Toast.LENGTH_LONG).show();
                    }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void ChangeName(String income)
    {
        String ret;
        switch (income) {
            case "http://orig09.deviantart.net/56d0/f/2015/294/2/a/ezgif_com_gif_maker_by_misscipcake-d9dy9pt.gif":
                ret = "SLEEP";
                break;
            case "https://emmajanemason.files.wordpress.com/2016/08/panda.gif?w=640":
                ret = "cute";
                break;
            case "https://media.giphy.com/media/13X8e2Af13yoZq/giphy.gif":
                ret = "workout";
                break;
            case "http://i1.kym-cdn.com/photos/images/newsfeed/001/011/272/8b7.gif":
                ret = "study";
                break;
            case "http://static.tumblr.com/bdac3decd6d4ac959db972dfa967a1e5/saekqib/xdonrwfrb/tumblr_static_1f7r9y6u10kgg04gcks0sg4gg.gif":
                ret = "eating";
                break;
            case "http://dye.mychat.to/gif_img/game_04/dye_co_pa_75.gif":
                ret = "watting";
                break;
            case "http://www.dreamworks.com/kungfupanda/images/uploads/downloads/KFP_09-workout.gif":
                ret = "fe al kolia";
                break;
            default:ret="STATUS";
                break;
        }

            getActivity().setTitle(ret);
    }

    String getStoredPair()
    {
        return getActivity().getPreferences(MODE_PRIVATE).getString(getString(R.string.getPairPref), "null");
    }
    void ubdateStatus(String data)
    {
        final String ret;
        switch (data)
        {
            case "SLEEP" :
                ret = "http://orig09.deviantart.net/56d0/f/2015/294/2/a/ezgif_com_gif_maker_by_misscipcake-d9dy9pt.gif";
                break;
            case "cute":
                ret = "https://emmajanemason.files.wordpress.com/2016/08/panda.gif?w=640";
                break;
            case   "workout":
                ret = "https://media.giphy.com/media/13X8e2Af13yoZq/giphy.gif";
                break;
            case "study" :
                ret = "http://i1.kym-cdn.com/photos/images/newsfeed/001/011/272/8b7.gif";
                break;
            case "eating" :
                ret = "http://static.tumblr.com/bdac3decd6d4ac959db972dfa967a1e5/saekqib/xdonrwfrb/tumblr_static_1f7r9y6u10kgg04gcks0sg4gg.gif";
                break;
            case "watting" :
                ret = "http://dye.mychat.to/gif_img/game_04/dye_co_pa_75.gif";
                break;
            case "fe al kolia" :
                ret = "http://www.dreamworks.com/kungfupanda/images/uploads/downloads/KFP_09-workout.gif";
                break;
            default:ret="STATUS";
                break;
        }
        ContractAcc acc=new ContractAcc();
        FirebaseDatabase fdb=FirebaseDatabase.getInstance();
        final DatabaseReference fdbr=fdb.getReference().child("Status");
        Query query=fdbr.orderByChild("email").equalTo(acc.get_username().getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String key=dataSnapshot.getChildren().iterator().next().getKey();
                fdbr.child(key).child("statusGif").setValue(ret);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
//// TODO: 4/13/2017      done so far