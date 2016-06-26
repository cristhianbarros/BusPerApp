package com.busperapp.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.busperapp.R;
import com.busperapp.login.LoginPresenter;
import com.busperapp.login.LoginPresenterImpl;
import com.busperapp.util.Util;

public class LoginRegisterActivity extends AppCompatActivity implements LoginView {

    private EditText inputEmail, inputPassword, inputName, inputSurname;
    private TextView txtSignIn, txtSignUp, txtExist;
    private ProgressBar progressBar;
    private LoginPresenter loginPresenter;
    private LinearLayout container;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        inputEmail = (EditText) findViewById(R.id.editTxtEmail);
        inputPassword = (EditText) findViewById(R.id.editTxtPassword);
        inputName = (EditText) findViewById(R.id.editTxtName);
        inputSurname = (EditText) findViewById(R.id.editTxtSurname);
        txtExist =(TextView) findViewById(R.id.txtExisst);
        Util.UnderlineText(txtExist,"Ya tienes una cuenta? Inicia Session");
        //txtSignIn = (TextView) findViewById(R.id.btnSignin);
        txtSignUp = (TextView) findViewById(R.id.btnSignUp);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        container = (LinearLayout) findViewById(R.id.layoutMainContainer);

        loginPresenter = new LoginPresenterImpl(this);
        loginPresenter.onCreate();

        hideProgressBar();

    }

    @Override
    public void enableInputs() {
        setInputs(true);
    }

    @Override
    public void disableInputs() {
        setInputs(false);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void handleSignUp(View v) {

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String name = inputName.getText().toString();
        String surName = inputSurname.getText().toString();

        if(!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !surName.isEmpty())
        {
            loginPresenter.registerNewUser(email, password, name, surName);
        } else {
            Util.showSnackbar(v, "Todos los campos son obligatorios");
        }

    }

    @Override
    public void handleSignIn(View v) {

    }

    public void handleExist(View v) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void navigateToMainScreen() {

    }

    @Override
    public void loginError(String error) {

    }

    @Override
    public void newUserSuccess() {
        Snackbar.make(container, R.string.login_notice_message_signup, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void newUSerError(String error) {
        inputPassword.setText("");
        String msjError = String.format(getString(R.string.login_error_message_signup), error);
        inputPassword.setError(msjError);
    }

    private void setInputs(boolean enabled) {
        inputEmail.setEnabled(enabled);
        inputPassword.setEnabled(enabled);

        inputName.setEnabled(enabled);
        inputSurname.setEnabled(enabled);

        //txtSignIn.setEnabled(enabled);
        txtSignUp.setEnabled(enabled);
    }

}
