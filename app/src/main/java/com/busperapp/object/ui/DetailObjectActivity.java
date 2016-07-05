package com.busperapp.object.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.busperapp.MainActivity;
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

    private TextView txtEmail, txtTitle, txtDescription, txtDate, txtCategory;
    private ImageView imgViewObject;
    private ObjectLost mObjectLost;
    private FirebaseHelper mHelper;
    private DatabaseReference mRef;
    private Button btnDelete;
    private FloatingActionButton fabEditBtn;
    private static String key;
    private static Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_object);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detalle");

        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtCategory = (TextView) findViewById(R.id.txtCategory);
        imgViewObject = (ImageView) findViewById(R.id.imageObject);

        btnDelete = (Button) findViewById(R.id.btnDelete);
        fabEditBtn = (FloatingActionButton) findViewById(R.id.btnFabEdit);

        Intent i = getIntent();

        if (i.hasExtra("key")) {

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
                        txtDate.setText(mObjectLost.getCreatedAt());
                        txtCategory.setText(mObjectLost.getCategory());

                        storageRef.child("images/" + key).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                                            fabEditBtn.setVisibility(View.VISIBLE);
                                            btnDelete.setVisibility(View.VISIBLE);
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
        final AlertDialog.Builder Alertdialog = new AlertDialog.Builder(DetailObjectActivity.this);
        Alertdialog.setMessage("Estas seguro que deseas eliminar el objeto ?")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mRef.child(FirebaseHelper.OBJECT_LOST_PATH).child(mObjectLost.getKey()).removeValue();
                        StorageReference storageRef = mHelper.getmStorage().getReferenceFromUrl("gs://luminous-fire-2940.appspot.com");
                        storageRef.child("images/" + mObjectLost.getKey()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Util.showMessage(getApplicationContext(), "El Objeto se ha eliminado de manera exitosa");

                           }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                exception.getMessage();
                            }
                        });

                        startActivity(new Intent(DetailObjectActivity.this, MainActivity.class));


                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }

        }).show();
    }

    public void editObject(View v) {

        if (mObjectLost != null) {
            Intent i = new Intent(this, AddObjectActivity.class);
            i.putExtra("action", "edit");
            i.putExtra("mTitle", mObjectLost.getTitle());
            i.putExtra("mDescription", mObjectLost.getDescription());
            i.putExtra("mCategory", mObjectLost.getCategory());
            i.putExtra("mImage", mUri.toString());
            i.putExtra("mLatitude", mObjectLost.getUbicationLatLang().get("latitude"));
            i.putExtra("mLongitude", mObjectLost.getUbicationLatLang().get("longitude"));
            i.putExtra("mKey", mObjectLost.getKey());
            i.putExtra("mCreatedAt", mObjectLost.getCreatedAt());
            i.putExtra("mPostalCode", mObjectLost.getPostalCode());
            i.putExtra("mAddress", mObjectLost.getAddress());

            Util.showMessage(getApplicationContext(), mObjectLost.getCreatedAt());

            startActivity(i);
        }

    }

}
