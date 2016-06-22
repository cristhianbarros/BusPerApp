package com.busperapp.util;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.busperapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.StringTokenizer;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    Activity mActivity;
    TextView txtTitle, txtEmail;

    public CustomInfoWindow(Activity activity) {
        this.mActivity = activity;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        String title = marker.getTitle();
        String snippet = marker.getSnippet();

        StringTokenizer split = new StringTokenizer(snippet, "|");
        String email = split.nextToken();

        View v = mActivity.getLayoutInflater().inflate(R.layout.custom_info_window, null);

        txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        txtEmail = (TextView) v.findViewById(R.id.txtEmail);

        txtTitle.setText(title);
        txtEmail.setText(email);





        return v;
    }
}
