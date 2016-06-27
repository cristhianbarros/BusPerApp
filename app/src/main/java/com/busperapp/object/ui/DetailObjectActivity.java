package com.busperapp.object.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.busperapp.R;
import com.busperapp.entities.ObjectLost;
import com.busperapp.util.FirebaseHelper;
import com.busperapp.util.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;

public class DetailObjectActivity extends AppCompatActivity {

    private TextView txtEmail, txtTitle, txtDescription, txtDate;
    private ImageView imgViewObject;
    private ObjectLost mObjectLost;
    private FirebaseHelper mHelper;
    private DatabaseReference mRef;
    private Button btnDelete, btnEdit;
    private String key;
    private static Uri mUri;
    private LinearLayout mContainerButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_object);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        imgViewObject = (ImageView) findViewById(R.id.imageObject);

        btnEdit = (Button) findViewById(R.id.btnEditObject);
        btnDelete = (Button) findViewById(R.id.btnDeleteObject);

        mContainerButtons = (LinearLayout) findViewById(R.id.containerButtons);

        Intent i = getIntent();

        if(i.hasExtra("key")) {

            key = i.getExtras().getString("key");
            mHelper = FirebaseHelper.getInstance();
            mRef = mHelper.getmRef();
            final StorageReference storageRef = mHelper.getmStorage().getReferenceFromUrl("gs://luminous-fire-2940.appspot.com");

            Query result = mRef.child(FirebaseHelper.OBJECT_LOST_PATH).orderByChild("key").equalTo(key);

            result.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    mObjectLost = dataSnapshot.getValue(ObjectLost.class);

                    if (mObjectLost != null) {

                        txtEmail.setText(mObjectLost.getUser());
                        txtDescription.setText(mObjectLost.getDescription());
                        txtTitle.setText(mObjectLost.getTitle());

                        storageRef.child("images/"+key).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mUri = uri;
                                Glide.with(getApplicationContext()).load(uri).listener(new RequestListener<Uri, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                        if (mObjectLost.getUser().equals(mHelper.getAuthUserEmail())) {
                                            mContainerButtons.setVisibility(View.VISIBLE);
                                        }

                                        return false;
                                    }
                                })
                                        .into(imgViewObject);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Util.showMessage(getApplicationContext(), exception.getMessage());
                            }
                        });

                    }
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

    }

    public void deleteObject(View v) {

    }


    public void editObject(View v) {

        if(mObjectLost != null) {
            Intent i = new Intent(this, AddObject.class);
            i.putExtra("action", "edit");
            i.putExtra("mTitle", mObjectLost.getTitle());
            i.putExtra("mDescription", mObjectLost.getDescription());
            i.putExtra("mCategory", mObjectLost.getCategory());
            i.putExtra("mImage", mUri.toString());
            i.putExtra("mLatitude", mObjectLost.getUbicationLatLang().get("latitude"));
            i.putExtra("mLongitude", mObjectLost.getUbicationLatLang().get("longitude"));
            i.putExtra("mKey", mObjectLost.getKey());
            i.putExtra("mPostalCode", mObjectLost.getPostalCode());
            i.putExtra("mAddress", mObjectLost.getAddress());
            //Toast.makeText(this, mUri.toString(), Toast.LENGTH_SHORT).show();
            startActivity(i);
        }

    }

}
