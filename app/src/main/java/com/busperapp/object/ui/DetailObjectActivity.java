package com.busperapp.object.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.busperapp.R;
import com.busperapp.entities.ObjectLost;
import com.busperapp.util.FirebaseHelper;
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
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_object);

        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        imgViewObject = (ImageView) findViewById(R.id.imageObject);

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

                        Log.e("mObjectLost", key);
                        Log.e("mObjectLost", mObjectLost.toString());
                        txtEmail.setText(mObjectLost.getUser());
                        txtDescription.setText(mObjectLost.getDescription());
                        txtTitle.setText(mObjectLost.getTitle());

                        storageRef.child("images/"+key).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getApplicationContext()).load(uri).into(imgViewObject);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

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
}
