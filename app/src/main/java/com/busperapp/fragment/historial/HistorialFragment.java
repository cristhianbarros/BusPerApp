package com.busperapp.fragment.historial;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public  HistoricalAdapter historicalViewAdapter;
    private Map<String, Double> mUbicationLatLng;
    public HistorialFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_historial, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        listObjects = new ArrayList<>();

        Query QueryRef = mRef.child(FirebaseHelper.OBJECT_LOST_PATH).orderByChild("user").equalTo(FirebaseHelper.getInstance().getAuthUserEmail());

            QueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ObjectLost mObj = dataSnapshot.getValue(ObjectLost.class);
                HistorialFragment.listObjects.add(mObj);
                historicalViewAdapter = new HistoricalAdapter(HistorialFragment.listObjects);
                recyclerView.setAdapter(historicalViewAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                historicalViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;

    }
}
