package com.example.qenawi.am.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qenawi.am.Contract.ContractDepug;
import com.example.qenawi.am.Interface.FireBaseListener2;
import com.example.qenawi.am.Interface.custoomDinterface;
import com.example.qenawi.am.R;
import com.example.qenawi.am.Services.save_mg_serv;
import com.example.qenawi.am.Services.uBloodWallpaber;
import com.example.qenawi.am.adapters.RecyclerViewAdapterMainActivity;
import com.example.qenawi.am.alertdialog.CustomDialogClass;
import com.example.qenawi.am.modles.StorageWallpaber;
import com.example.qenawi.am.modles.WallPaberv;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class WALLPABER extends Fragment implements RecyclerViewAdapterMainActivity.onClickListner, FireBaseListener2,custoomDinterface {
    // TODO: Rename and change types of parameters
    //objects
    private FireBaseListener2 mCallback;
    private OnWallPaperFragmentInteractionListener mListener;
    private DatabaseReference dp_ref;
    private RecyclerView mainGrid;
    private RecyclerViewAdapterMainActivity adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> data;
    private ArrayList<String> data_camo;
    private int children_cnt = 0;

    public WALLPABER() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menueItemadd:
                UploodBG();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.wallpabermenu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View Root;
        Root = inflater.inflate(R.layout.fragment_wall_paper, container, false);
        mCallback = this;
        data = new ArrayList<>();
        data_camo=new ArrayList<>();
        mainGrid = (RecyclerView) Root.findViewById(R.id.wallpaperFragment_backgroundRV);
        layoutManager = new GridLayoutManager(getContext(),2);
        mainGrid.setHasFixedSize(true);
        mainGrid.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapterMainActivity(getContext(), this, data,data_camo, 1);
        mainGrid.setAdapter(adapter);
        //--------------------------------------------
        get_img_cnt();//->fetch img lnks
        return Root;
    }

    //------------------------------
//-----------------------
// TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onwallpaperFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWallPaperFragmentInteractionListener) {
            mListener = (OnWallPaperFragmentInteractionListener) context;
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

    //-----------------------------listners-----------------------------------------------------
    @Override
    public void onListItemClick(int Clickpos)
    {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        CustomDialogClass cdd=new CustomDialogClass(getActivity(),data.get(Clickpos),data_camo.get(Clickpos),this);
        cdd.show();
        cdd.getWindow().setLayout((6 * width)/7, (4 * height)/5);

      //  ubdate_bg(data.get(Clickpos), getStoredPair());
    }

    @Override
    public void onDone2(Object O, String TAG) {

// add to data
        if (getString(R.string.getImgUri).equals(TAG))//when image link is ready
        {
            ArrayList<Uri>Ooo=(ArrayList<Uri>)O;
          //  Uri da = (Uri) O;
            data.add(Ooo.get(0).toString());
            data_camo.add(Ooo.get(1).toString());
            // adapter.notifyDataSetChanged();
        } else if (getString(R.string.getImgUri01).equals(TAG))//whenn all imgs urls loaded successfully
        {
            adapter.notifyDataSetChanged();
            if (getActivity() != null)
            {
                getActivity().setTitle("wallpaper's (" + adapter.getItemCount() + ")");
            }
        } else if (getString(R.string.getImgUri02).equals(TAG)) {
            // o -> children cnt
            // call get imgs o by o till we hit cnt
            Log.v(ContractDepug.PUBTAG, "fetch call");
            ftch_imgslnks((int) O);

        }
    }

    @Override
    public void DoAction(Object O, String Tag)
    {
        if (Tag.equals("save"))
        {
       save_to_sd((String)O);
        }
        else
        {
            ubdate_bg((String) O, getStoredPair());
        }
    }

    public interface OnWallPaperFragmentInteractionListener {
        void onwallpaperFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Integer.valueOf(getString(R.string.selectPhotoIntent)))
        {
            if (resultCode == RESULT_OK)
            {
                Uri Imguri = data.getData();
                UploodBGHelper(Imguri);
            }
            else
            {
                Toast.makeText(getActivity(), "Faild to get img", Toast.LENGTH_SHORT).show();
            }

        }

    }

    //------------------------functions-----------------------------------------
    private void UploodBG() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Integer.valueOf(getString(R.string.selectPhotoIntent)));
    }
    private void UploodBGHelper(Uri uri) {
        // start uplood service
        getActivity().startService(new Intent(getActivity(), uBloodWallpaber.class)
                .putExtra(getString(R.string.uBloodwallpaperServiceintent), uri.toString())
                .setAction("ACTION"));
    }
    private void save_to_sd(String uri)
    {
        // start uplood service
        getActivity().startService(new Intent(getActivity(), save_mg_serv.class)
                .putExtra("ZAMPACTO", uri)
                .setAction("ACTION"));
    }

    String getStoredPair() {
        return getActivity().getPreferences(MODE_PRIVATE).getString(getString(R.string.getPairPref), "null");
    }

    private void ubdate_bg(final String data, final String email2) {
        dp_ref = FirebaseDatabase.getInstance().getReference().child("wallpaber");
        Query query1 = dp_ref.orderByChild("email").equalTo(email2);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                WallPaberv f;
                String key = "erorr";
                for (DataSnapshot election : dataSnapshot.getChildren()) {

                    key = election.getKey();
                }
                dp_ref.child(key).child("link").setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "WallPaberUpdated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Faild to Update", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void get_img_url(final int curnt, final int totalcnt, String img_name, final String folderName, final String img_camo)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
// Points to the root reference
        StorageReference storageRef = storage.getReference();
// Points to "images"
        StorageReference imagesRef = storageRef.child(folderName);
        final StorageReference imagesRef2 = storageRef.child(folderName);
        imagesRef.child(img_name).getDownloadUrl().addOnSuccessListener
                (new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                    get_img_camo_url(uri,folderName,img_camo);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v(ContractDepug.PUBTAG, e.getMessage());
            }
        });
    }
    void get_img_camo_url(final Uri base, String folderName, String img_camo)
    {
        //get camo
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference imagesRef2 = storageRef.child(folderName);
        imagesRef2.child(img_camo).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri uri)
            {
                ArrayList<Uri>Oo=new ArrayList<Uri>();
                Oo.add(base);
                Oo.add(uri);
                if (getActivity() != null)
                {
                    mCallback.onDone2(Oo, getString(R.string.getImgUri));
                    mCallback.onDone2(null, getString(R.string.getImgUri01));
                }
            }
        });
    }
    //---------------------------------
    void ftch_imgslnks(final int chldcnt)
    {

        Log.v(ContractDepug.PUBTAG, "fetch img links");
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference fdbr = fdb.getReference().child("Storage").child("wallbaber");
        fdbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //     Log.v(ContractDepug.PUBTAG,children_cnt+" => "+chldcnt);
                children_cnt++;
                get_img_url(children_cnt, chldcnt,dataSnapshot.getValue(StorageWallpaber.class).getUri(), "wallbapers",dataSnapshot.getValue(StorageWallpaber.class).getUri_camo());
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

    void get_img_cnt() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference fdbr = fdb.getReference().child("Storage").child("wallbaber");
        fdbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int cnt = (int) dataSnapshot.getChildrenCount();
                if (getActivity() != null) {
                    mCallback.onDone2(cnt, getString(R.string.getImgUri02));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onPause() {


        super.onPause();
    }

}

// get imguri when load one img url
//              01       all img and ubdate adp
//              02       fetching img  img cnt done