package com.busperapp.login;

import android.util.Log;

import com.busperapp.util.FirebaseHelper;

public class LoginRepositoryImpl  implements LoginRepository {

    FirebaseHelper helper;

    public LoginRepositoryImpl() {
        this.helper = FirebaseHelper.getInstance();
    }

    @Override
    public void signUp(String email, String password) {
        Log.e("LoginRepository", "signUp");
    }

    @Override
    public void signIn(String email, String password) {
        Log.e("LoginRepository", "signIn");
    }

    @Override
    public void checkSession() {
        Log.e("LoginRepository", "checkIn");
    }
}
