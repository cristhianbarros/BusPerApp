package com.busperapp.login.ui;

import android.view.View;

public interface LoginView {

    void enableInputs();
    void disableInputs();

    void showProgressBar();
    void hideProgressBar();

    void handleSignUp(View v);
    void handleSignIn(View v);

    void navigateToMainScreen();
    void loginError(String error);

    void newUserSuccess();
    void newUSerError(String error);



}
