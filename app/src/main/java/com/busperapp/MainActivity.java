package com.busperapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.busperapp.entities.Profile;
import com.busperapp.fragment.historial.HistorialFragment;
import com.busperapp.fragment.map.MapFragment;
import com.busperapp.fragment.profile.ProfileFragment;
import com.busperapp.login.LoginInteractor;
import com.busperapp.login.LoginInteractorImpl;
import com.busperapp.login.ui.LoginActivity;
import com.busperapp.util.FirebaseHelper;
import com.busperapp.util.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static FragmentManager mFramgmentManager;
    private Fragment mFragment;
    private LoginInteractor loginInteractor;
    public static Toolbar toolbar;
    private TextView txtFullNames, txtEmail;
    private Uri mUri;
    private ImageView photoProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mapa");

        loginInteractor = new LoginInteractorImpl();

        if (savedInstanceState == null) {
            mFragment = new MapFragment();
            mFramgmentManager = getSupportFragmentManager();

            mFramgmentManager.beginTransaction()
                    .add(R.id.main_content, mFragment)
                    .addToBackStack("Map Fragment 1")
                    .commit();
        }

        Query QueryRef = FirebaseHelper.getInstance().getmRef().child(FirebaseHelper.PROFILES_PATH).orderByChild("email").equalTo(FirebaseHelper.getInstance().getAuthUserEmail());

        QueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Profile profile = dataSnapshot.getValue(Profile.class);

                if (profile != null) {

                    String fullNames = profile.getNames() + " " + profile.getSurnames();
                    String email = profile.getEmail();

                    txtFullNames = (TextView) findViewById(R.id.txtFullNames);
                    txtEmail = (TextView) findViewById(R.id.txtEmail);

                    txtFullNames.setText(fullNames);
                    txtEmail.setText(email);

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

        String mEmail = FirebaseHelper.getInstance().getAuthUserEmail();
        final StorageReference storageReference = FirebaseHelper.getInstance().getmStorage().getReferenceFromUrl("gs://luminous-fire-2940.appspot.com");

        storageReference.child("profile/" + mEmail).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                photoProfile = (ImageView) findViewById(R.id.imgViewPhoto);
                Glide
                    .with(getApplicationContext())
                    .load(uri)
                    .into(photoProfile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Util.showMessage(getApplicationContext(), exception.getMessage());
            }
        });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, new MapFragment())
                    .commit();

        } else if (id == R.id.nav_history) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, new HistorialFragment())
                    .commit();
        } else if (id == R.id.nav_profile) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, new ProfileFragment())
                    .commit();

        } else if (id == R.id.nav_sign_out) {
            loginInteractor.doSignOut();
            startActivity(new Intent(this, LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
