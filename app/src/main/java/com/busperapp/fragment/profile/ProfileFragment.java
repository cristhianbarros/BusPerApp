package com.busperapp.fragment.profile;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alexzh.circleimageview.CircleImageView;
import com.bumptech.glide.Glide;
import com.busperapp.MainActivity;
import com.busperapp.R;
import com.busperapp.entities.Profile;
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
import com.google.firebase.storage.UploadTask;
import com.rey.material.widget.Switch;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {

    private Switch switchProfile;
    private TextView txtFullNames, txtEmail;
    private EditText inputCellphone;
    private CircleImageView imgViewPhoto;
    private FloatingActionButton fabSaveProfile;
    private Uri path, mUri;
    Boolean isPublic;
    String email;
    Profile profile;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        txtFullNames = (TextView) v.findViewById(R.id.txtFullNames);
        txtEmail = (TextView) v.findViewById(R.id.txtEmail);
        fabSaveProfile = (FloatingActionButton) v.findViewById(R.id.fabSaveProfile);
        imgViewPhoto = (CircleImageView) v.findViewById(R.id.imgViewPhoto);
        inputCellphone = (EditText) v.findViewById(R.id.inputCellPhone);
        switchProfile = (Switch) v.findViewById(R.id.switchPublicProfile);

        email = FirebaseHelper.getInstance().getAuthUserEmail();

        final StorageReference storageReference = FirebaseHelper.getInstance().getmStorage().getReferenceFromUrl("gs://luminous-fire-2940.appspot.com");

        storageReference.child("profile/" + email).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                mUri = uri;
                Glide.with(getContext()).load(uri)
                        .into(imgViewPhoto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Util.showMessage(getContext(), exception.getMessage());
            }
        });

        Query QueryRef = FirebaseHelper.getInstance().getmRef().child(FirebaseHelper.PROFILES_PATH).orderByChild("email").equalTo(FirebaseHelper.getInstance().getAuthUserEmail());

        QueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                profile = dataSnapshot.getValue(Profile.class);

                if (profile != null) {

                    String fullNames = profile.getNames() + " " + profile.getSurnames();
                    String email = profile.getEmail();
                    String cellPhone = profile.getCellPhone();
                    Boolean show = profile.getPublic();

                    txtFullNames.setText(fullNames);
                    txtEmail.setText(email);
                    inputCellphone.setText(cellPhone);

                    switchProfile.setChecked(show);

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

        fabSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference mRef = FirebaseHelper.getInstance().getmRef().child(FirebaseHelper.PROFILES_PATH);
                String cellPhone = inputCellphone.getText().toString();

                isPublic = switchProfile.isChecked();

                Profile updatedProfile = new Profile();
                updatedProfile.setNames(profile.getNames());
                updatedProfile.setSurnames(profile.getSurnames());
                updatedProfile.setCellPhone(cellPhone);
                updatedProfile.setEmail(email);
                updatedProfile.setPublic(isPublic);

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(email.replace(".","_"), updatedProfile.toMap());

                mRef.updateChildren(childUpdates);

                if(imgViewPhoto != null) {
                    StorageReference objectLostPhoto = storageReference.child("profile/"+updatedProfile.getEmail());
                    imgViewPhoto.setDrawingCacheEnabled(true);
                    imgViewPhoto.buildDrawingCache();
                    Bitmap bitmap = imgViewPhoto.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = objectLostPhoto.putBytes(data);

                }

                Util.showMessage(getContext(), "Tú perfil ha sido modificado de manera exitosa.");
                startActivity(new Intent(getContext(), MainActivity.class));

            }
        });

        imgViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 200);
            }
        });


        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 200:
                if (resultCode == Activity.RESULT_OK) {
                    path = data.getData();
                    if (path != null) {
                        Glide.with(this).load(path).into(imgViewPhoto);
                    }
                }
                break;
            default:
                Log.e("onActivityResult", "No Entro");
                break;

        }

    }

}
