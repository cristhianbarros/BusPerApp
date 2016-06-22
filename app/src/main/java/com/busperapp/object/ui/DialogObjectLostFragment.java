package com.busperapp.object.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.busperapp.R;
import com.busperapp.entities.ObjectLost;
import com.busperapp.model.Category;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DialogObjectLostFragment extends DialogFragment {

    private Geocoder mGeoCoder;
    private List<Address> mAdressses;

    private Bundle mBundle;
    private Double mLatitude, mLongitude;
    private EditText edtTitle, edtDescription, edtAddress;
    private ImageButton btnImage;
    private String address, postalCode, mCurrentPhotoPath;
    private FirebaseDatabase mDatabase;
    private ObjectLost mObjectLost;
    private Map<String, Double> mUbicationLatLng;
    private Spinner spinnerCategory;
//    private ArrayList<Category> mArrayCategories;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        mBundle = getArguments();
        mLatitude = mBundle.getDouble("latitude");
        mLongitude = mBundle.getDouble("longitude");

        final View v = inflater.inflate(R.layout.fragment_object_lost, null);

        edtTitle = (EditText) v.findViewById(R.id.edtTitle);
        edtDescription = (EditText) v.findViewById(R.id.edtDescription);
        edtAddress = (EditText) v.findViewById(R.id.edtAddress);
        btnImage = (ImageButton) v.findViewById(R.id.btnImage);
        spinnerCategory = (Spinner) v.findViewById(R.id.spinnerCategory);

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                getActivity().startActivityForResult(i, 200);
            }
        });

        new LoadSettings().execute();

        builder.setView(v)
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        mUbicationLatLng = new HashMap<>();
//                        mUbicationLatLng.put("latitude", mLatitude);
//                        mUbicationLatLng.put("longitude", mLongitude);
//
//                        Category mCategory = (Category) spinnerCategory.getSelectedItem();
//
//                        mObjectLost = new ObjectLost(edtTitle.getText().toString(), edtDescription.getText().toString(), edtAddress.getText().toString(), mCategory.getName(),postalCode, mUbicationLatLng);
//                        mObjectLostAdd.push().setValue(mObjectLost);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }


    private class LoadSettings extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            mGeoCoder = new Geocoder(getContext(), Locale.getDefault());

            try {

                mAdressses =  mGeoCoder.getFromLocation(mLatitude, mLongitude, 1);
                address = mAdressses.get(0).getAddressLine(0);
                postalCode = mAdressses.get(0).getPostalCode();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                Log.e("OutBounds: ", e.getMessage());
                e.printStackTrace();
            }

            DatabaseReference mCategoryRef = mDatabase.getReference(Category.FIREBASE_TAG);
//            mArrayCategories = new ArrayList<>();

            Query mRef = mCategoryRef.orderByChild("active").equalTo(true);

            mRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    Category c = dataSnapshot.getValue(Category.class);
//                    mArrayCategories.add(c);
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


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            edtAddress.setText(address);
//            ArrayAdapter<Category> mAdapter = new ArrayAdapter<Category>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, mArrayCategories);
//            spinnerCategory.setAdapter(mAdapter);
//            spinnerCategory.setSelection(0);

        }
    }


}
