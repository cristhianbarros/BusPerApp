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
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddObject extends AppCompatActivity {

    private static String APP_DIRECTORY = "MyPictureApp/";

    private String mPath,   address, postalCode, uniqueKey, msj;
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
    private ObjectLost objectLost;
    Map<String, Double> ubicationLatLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_object);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIntent = getIntent();

        mSetImage = (ImageView) findViewById(R.id.imgViewPhoto);
        mSetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 200);
            }
        });

        if(!mIntent.hasExtra("action")) {
            initComponents(null);
            if (mIntent.hasExtra("latitude") && mIntent.hasExtra("longitude")) {
                mLatitude = mIntent.getExtras().getDouble("latitude");
                mLongitude = mIntent.getExtras().getDouble("longitude");
            }

            new LoadSettings().execute();

        } else {
            initComponents(mIntent.getExtras().getString("action"));
            mLatitude = mIntent.getExtras().getDouble("mLatitude");
            mLongitude = mIntent.getExtras().getDouble("mLongitude");
        }

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

    public void initComponents(String edit) {

        inputTitle = (EditText) findViewById(R.id.editTxtTitle);
        inputDescription = (EditText) findViewById(R.id.editTextDescription);
        inputAddress = (EditText) findViewById(R.id.editTxtAddress);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        fabSaveObject = (FloatingActionButton) findViewById(R.id.fabSaveObject);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        if(edit != null) {
            fabSaveObject.setVisibility(View.GONE);
            inputTitle.setText(mIntent.getExtras().getString("mTitle"));
            inputDescription.setText(mIntent.getExtras().getString("mDescription"));
            inputAddress.setText(mIntent.getExtras().getString("mAddress"));

            spinnerCategory.setSelection(adapter.getPosition(mIntent.getExtras().getString("mCategory")));
            Glide.with(this)
                    .load(mIntent.getExtras().getString("mImage"))
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            fabSaveObject.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(mSetImage);

        }

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

        if(mIntent.hasExtra("action")) {
            uniqueKey = mIntent.getExtras().getString("mKey");

            objectLost = new ObjectLost();
            objectLost.setKey(uniqueKey);
            objectLost.setTitle(title);
            objectLost.setDescription(description);
            objectLost.setAddress(inputAddress.getText().toString());
            objectLost.setPostalCode(mIntent.getExtras().getString("mPostalCode"));
            objectLost.setCategory(category);
            objectLost.setCreatedAt(mIntent.getExtras().getString("mCreatedAt"));
            objectLost.setUbicationLatLang(ubicationLatLang);
            objectLost.setUser(user);

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(uniqueKey, objectLost.toMap());

            mRef.updateChildren(childUpdates);

            msj = "Tu objeto extraviado ha sido modificado de manera exitosa.";

        } else {
            uniqueKey = mRef.push().getKey();

            Date dNow = new Date( );

            objectLost = new ObjectLost();
            objectLost.setKey(uniqueKey);
            objectLost.setTitle(title);
            objectLost.setDescription(description);
            objectLost.setAddress(address);
            objectLost.setPostalCode(postalCode);
            objectLost.setCategory(category);
            objectLost.setUbicationLatLang(ubicationLatLang);
            objectLost.setCreatedAt(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(dNow));
            objectLost.setUser(user);

            mRef.child(uniqueKey).setValue(objectLost);

            msj = "Tu objeto extraviado ha sido almacenado de manera exitosa";
        }

        if(mSetImage != null) {
            StorageReference objectLostPhoto = storageReference.child("images/"+objectLost.getKey());
            mSetImage.setDrawingCacheEnabled(true);
            mSetImage.buildDrawingCache();
            Bitmap bitmap = mSetImage.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = objectLostPhoto.putBytes(data);

        }

        Util.showMessage(getApplicationContext(), msj);
        startActivity(new Intent(this, MainActivity.class));

    }

}
