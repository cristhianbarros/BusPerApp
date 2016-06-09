package com.busperapp.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cristhian.barros on 14/06/2016.
 */
public class FirebaseHelper {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuthData;
    private final static String USER_PATH = "users";
    private final static String OBJECT_LOST_PATH = "objectlost";
    private final static String CATEGORIES_PATH = "categories";
    private final static String FIREBASE_URL = "https://luminous-fire-2940.firebaseio.com/";

    private static class SingletonHolder {
        private static final FirebaseHelper INSTANCE = new FirebaseHelper();
    }

    public static FirebaseHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public FirebaseHelper() {
        this.mDatabase = FirebaseDatabase.getInstance();
        this.mRef = this.mDatabase.getReference();
    }

    public DatabaseReference getmRef() {
        return mRef;
    }

    public String getAuthUserEmail() {
        mAuthData = FirebaseAuth.getInstance();
        String email = null;
        if(mAuthData != null) {
            if(mAuthData.getCurrentUser() != null) {
                email = mAuthData.getCurrentUser().getEmail();
            }
        }

        return email;
    }

    public DatabaseReference getUserReference(String email) {
        DatabaseReference userReference = null;
        if(email != null) {
            String emailKey = email.replace(".","_");
            userReference = mDatabase.getReference().child(USER_PATH).child(emailKey);
        }

        return userReference;

    }

    public DatabaseReference getMyUserReference() {
        return getUserReference(getAuthUserEmail());
    }


    public void changeUserConnectionStatus(boolean online) {
        if(getMyUserReference() != null) {
            Map<String, Object> updates = new HashMap<>();
            updates.put("online", online);
            getMyUserReference().updateChildren(updates);
            notifyWhenOnConnectionChange(online);
        }
    }

    public void notifyWhenOnConnectionChange(boolean online) {
        if(!online) {
            signOff();
        }
    }

    public void signOff() {
        mAuthData.signOut();
    }


}
