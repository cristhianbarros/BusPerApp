package com.busperapp.fragment.map;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.busperapp.R;
import com.busperapp.entities.ObjectLost;
import com.busperapp.object.ui.AddObject;
import com.busperapp.object.ui.DetailObjectActivity;
import com.busperapp.util.CustomInfoWindow;
import com.busperapp.util.FirebaseHelper;
import com.busperapp.util.GoogleApiClientHelper;
import com.busperapp.util.Util;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Map;
import java.util.StringTokenizer;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    protected GoogleApiClientHelper mGoogleApiClientHelper;
    protected Double mLatitude, mLongitude;
    protected Context mContext;
    protected FirebaseDatabase mDatabase;
    protected DatabaseReference mRef;

    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getContext();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        View v = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        // In this part is important to handle the exceptions
        // For this reason I include a try catch to handle the Security Exception

        try {
            mMap = mapFragment.getMap();
            mMap.setMyLocationEnabled(true);

            mGoogleApiClientHelper = new GoogleApiClientHelper(mContext, mMap);
            mapFragment.getMapAsync(this);

        } catch (SecurityException e) {
            e.printStackTrace();
            Util.showMessage(mContext, e.getMessage());
        }

        return v;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                mLatitude = latLng.latitude;
                mLongitude = latLng.longitude;

                Intent mIntent = new Intent(getContext(), AddObject.class);
                mIntent.putExtra("latitude", mLatitude);
                mIntent.putExtra("longitude", mLongitude);
                startActivity(mIntent);

            }
        });


        mMap.setInfoWindowAdapter(new CustomInfoWindow(getActivity()));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                StringTokenizer tokens = new StringTokenizer(marker.getSnippet(), "|");

                String email = tokens.nextToken();
                String key = tokens.nextToken();

                Intent i = new Intent(getContext(), DetailObjectActivity.class);
                i.putExtra("key", key);

                startActivity(i);


            }
        });

//
//        Query queryRef = mRef.child("object_lost").orderByChild("ubicationLatLang/latitud").startAt(6.233).endAt(6.235);
        Query queryRef = mRef.child(FirebaseHelper.OBJECT_LOST_PATH).orderByKey();

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                ObjectLost objectLost = dataSnapshot.getValue(ObjectLost.class);
                Map<String, Double> ubicationObject = objectLost.getUbicationLatLang();

                String snippet = objectLost.getUser()+"|"+objectLost.getKey();

                MarkerOptions mMarkerOption = new MarkerOptions()
                        .position(new LatLng(ubicationObject.get("latitude"), ubicationObject.get("longitude")))
                        .title(objectLost.getTitle())
                        .snippet(snippet)
                        ;


                Marker mMarker = mMap.addMarker(mMarkerOption);


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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}