package com.busperapp.object.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.busperapp.MainActivity;
import com.busperapp.R;
import com.busperapp.entities.ObjectLost;
import com.busperapp.util.FirebaseHelper;
import com.busperapp.util.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddObject extends AppCompatActivity {

    private static String APP_DIRECTORY = "MyPictureApp/";

    private String mPath, address, postalCode;
    private ImageView mSetImage;
    private EditText inputTitle, inputDescription, inputAddress;
    private Spinner spinnerCategory;
    private FloatingActionButton fabSaveObject;
    private Map<String, Double> mUbicationLatLng;
    private Geocoder mGeoCoder;
    private List<Address> mAdressses;
    private Double mLatitude, mLongitude;
    private Intent mIntent;
    private static Uri path;
    Map<String, Double> ubicationLatLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_object);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponents();

        mIntent = getIntent();

        if (mIntent.hasExtra("latitude") && mIntent.hasExtra("longitude")) {
            mLatitude = mIntent.getExtras().getDouble("latitude");
            mLongitude = mIntent.getExtras().getDouble("longitude");
        }

        new LoadSettings().execute();

        mSetImage = (ImageView) findViewById(R.id.imgViewPhoto);
        mSetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 200);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 200:
                if (resultCode == RESULT_OK) {
                    path = data.getData();

                    if (path != null) {
                        Glide.with(this).load(path).into(mSetImage);
                    }
                }
                break;
            default:
                Log.e("onActivityResult", "No Entro");
                break;

        }

    }

    private class LoadSettings extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            mGeoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {
                if (mLatitude != null && mLongitude != null) {
                    mAdressses = mGeoCoder.getFromLocation(mLatitude, mLongitude, 1);
                    address = mAdressses.get(0).getAddressLine(0);
                    postalCode = mAdressses.get(0).getPostalCode();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            inputAddress.setText(address);
        }

    }

    public void initComponents() {

        inputTitle = (EditText) findViewById(R.id.editTxtTitle);
        inputDescription = (EditText) findViewById(R.id.editTextDescription);
        inputAddress = (EditText) findViewById(R.id.editTxtAddress);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        fabSaveObject = (FloatingActionButton) findViewById(R.id.fab);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

    }

    public void saveObject(final View v) {

        FirebaseHelper helper = new FirebaseHelper(FirebaseHelper.OBJECT_LOST_PATH);
        final StorageReference storageReference = helper.getmStorage().getReferenceFromUrl("gs://luminous-fire-2940.appspot.com");

        String title = inputTitle.getText().toString();
        String description = inputDescription.getText().toString();
        String category = spinnerCategory.getSelectedItem().toString();
        String user = helper.getAuthUserEmail();

        ubicationLatLang = new HashMap<>();
        ubicationLatLang.put("latitude", mLatitude);
        ubicationLatLang.put("longitude", mLongitude);

        DatabaseReference mRef = helper.getmRef();

        String uniqueKey = mRef.push().getKey();

        ObjectLost objectLost = new ObjectLost();
        objectLost.setKey(uniqueKey);
        objectLost.setTitle(title);
        objectLost.setDescription(description);
        objectLost.setAddress(address);
        objectLost.setPostalCode(postalCode);
        objectLost.setCategory(category);
        objectLost.setUbicationLatLang(ubicationLatLang);
        objectLost.setUser(user);

        mRef.child(uniqueKey).setValue(objectLost);


        if(mSetImage != null) {
            StorageReference objectLostPhoto = storageReference.child("images/"+objectLost.getKey());

            mSetImage.setDrawingCacheEnabled(true);
            mSetImage.buildDrawingCache();
            Bitmap bitmap = mSetImage.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = objectLostPhoto.putBytes(data);

            if (uploadTask.isSuccessful()) {
                Util.showSnackbar(v, "Tu objeto extraviado ha sido almacenado de manera exitosa");
                startActivity(new Intent(this, MainActivity.class));
            }
        }

    }

}
