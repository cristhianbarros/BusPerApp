package com.busperapp.util;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;


public class GoogleApiClientHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    protected final static String TAG = GoogleApiClientHelper.class.getName();

    private GoogleApiClient mGoogleApiClient;
    private Double mLatitude, mLongitude;
    private Context mContext;
    private final int  REQUEST_RESOLVE_GOOGLE_CLIENT_ERROR = 1;
    private boolean mResolvingError;
    private GoogleMap mMap;
    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters



    public GoogleApiClientHelper(Context c, GoogleMap map) {
        this.mContext = c;
        this.mMap = map;

        setupGoogleApiClient();
    }

    public synchronized void setupGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    public void getLocation() {

        try {

            Location mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

            if(mLastLocation != null) {

                mLatitude = mLastLocation.getLatitude();
                mLongitude = mLastLocation.getLongitude();

                LatLng mLatLng = new LatLng(mLatitude, mLongitude);

                CameraUpdate mUpdateLocation = CameraUpdateFactory.newLatLngZoom(mLatLng, 18);
                mMap.animateCamera(mUpdateLocation);

            } else {
                Util.showMessage(mContext,"Ops, no ha sido posible encontrar tú Ubicación");
            }

        } catch(SecurityException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        Log.i(TAG, " onConnectionSuspended() called");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, " onConnectionFailed() called, message: "+connectionResult.toString());
        mGoogleApiClient.disconnect();
    }

    public void disconnect() {
        mGoogleApiClient.disconnect();
    }

}
