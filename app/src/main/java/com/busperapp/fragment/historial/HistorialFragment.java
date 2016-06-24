package com.busperapp.fragment.historial;


import android.app.DownloadManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.busperapp.R;
import com.busperapp.entities.ObjectLost;
import com.busperapp.util.FirebaseHelper;
import com.busperapp.util.HistoricalAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistorialFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    public static ArrayList<ObjectLost> listObjects;
    private HistoricalAdapter historicalViewAdapter;
    private Map<String, Double> mUbicationLatLng;

    public HistorialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_historial, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        listObjects = new ArrayList<ObjectLost>();
        Query QueryRef = mRef.child(FirebaseHelper.OBJECT_LOST_PATH).orderByChild("user");//.equalTo("cristhianbarros91@hotmail.com");

        QueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ObjectLost mObj = dataSnapshot.getValue(ObjectLost.class);
                HistorialFragment.listObjects.add(mObj);
                historicalViewAdapter = new HistoricalAdapter(HistorialFragment.listObjects);
                recyclerView.setAdapter(historicalViewAdapter);

                //listObjects.add(new ObjectLost(mObj.getTitle(),mObj.getDescription(),mObj.getAddress(),mObj.getCategory(),mObj.getPostalCode(),mObj.getUbicationLatLang(),mObj.getUser()))
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

        /*mUbicationLatLng = new HashMap<>();
        mUbicationLatLng.put("latitude", 6.212419);
        mUbicationLatLng.put("longitude", -75.573792);
        HistorialFragment.listObjects.add(new ObjectLost("prueba", "primer vista", "Diagonal 62", "hola", "12345", mUbicationLatLng, "agrajava"));
        HistorialFragment.listObjects.add(new ObjectLost("prueba2", "segunda vista", "Diagonal 62", "hola", "12345", mUbicationLatLng, "agrajava"));
        */

        return v;


//        Category c = new Category("DOCUMENTOS","", true);
//        Category c2 = new Category("ANIMAL","", true);
//        Category c3 = new Category("VEHICULO","", true);
//        Category c4 = new Category("OTROS","", true);
//
//
//        ArrayList<Category> mArrayList = new ArrayList<>();
//        mArrayList.add(c);
//        mArrayList.add(c2);
//        mArrayList.add(c3);
//        mArrayList.add(c4);
//
//        for(int i = 0; i < mArrayList.size(); i++) {
//            mRef.child("categories").push().setValue(mArrayList.get(i));
//        }

        // Inflate the layout for this fragment


//        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference mRef = mDatabase.getReference();
//
//
//        Query queryRef = mRef.child("ObjectLost").orderByChild("ubicationLatLang/latitude").startAt(6.233).endAt(6.235);
//
//        queryRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                ObjectLost o = dataSnapshot.getValue(ObjectLost.class);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//
//        });
    }
}
