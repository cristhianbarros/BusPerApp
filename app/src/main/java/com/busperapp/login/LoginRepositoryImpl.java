package com.busperapp.login;

import android.support.annotation.NonNull;

import com.busperapp.entities.User;
import com.busperapp.login.events.LoginEvent;
import com.busperapp.util.EventBus;
import com.busperapp.util.FirebaseHelper;
import com.busperapp.util.GreenRobotEventBus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginRepositoryImpl implements LoginRepository {

    FirebaseHelper helper;
    FirebaseAuth mAuth;
    DatabaseReference mUserReference;

    public LoginRepositoryImpl() {
        this.helper = FirebaseHelper.getInstance();
        this.mUserReference = helper.getMyUserReference();
        this.mAuth = helper.getmAuthData();

    }

    @Override
    public void signUp(final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {
                        postEvent(LoginEvent.onSignUpError, task.getException().toString());
                    } else {
                        postEvent(LoginEvent.onSignUpSuccess);
                        signIn(email, password);
                    }
                    }
                });
    }

    @Override
    public void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            postEvent(LoginEvent.onSignInError, task.getException().toString());
                        } else {
                            initSignIn();
                        }


                    }
                });
    }

    @Override
    public void checkSession() {

        if (mAuth.getCurrentUser() != null) {
            initSignIn();
        } else {
            postEvent(LoginEvent.onFailedRecoverSession);
        }
    }

    @Override
    public void signOut() {
        mAuth.signOut();
        helper.changeUserConnectionStatus(User.OFFLINE);
    }

    private void initSignIn() {
        mUserReference = helper.getMyUserReference();
        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);

                if (currentUser == null) {
                    registerNewUser();
                }

                helper.changeUserConnectionStatus(User.ONLINE);
                postEvent(LoginEvent.onSignInSuccess);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void registerNewUser() {
        String email = helper.getAuthUserEmail();

        if (email != null) {
            User currentUser = new User();
            currentUser.setEmail(email);
            mUserReference.setValue(currentUser);
        }
    }

    private void postEvent(int type, String errorMessage) {
        LoginEvent loginEvent = new LoginEvent();
        loginEvent.setEventType(type);

        if (errorMessage != null) {
            loginEvent.setErrorMessage(errorMessage);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(loginEvent);


    }

    private void postEvent(int type) {
        postEvent(type, null);
    }
}
