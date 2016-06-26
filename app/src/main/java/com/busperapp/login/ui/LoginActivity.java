package com.busperapp.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.busperapp.MainActivity;
import com.busperapp.R;
import com.busperapp.login.LoginPresenter;
import com.busperapp.login.LoginPresenterImpl;


public class LoginActivity extends AppCompatActivity implements LoginView {


    EditText inputEmail, inputPassword;
    TextView btnSignIn,btnSignUp;
    ProgressBar progressBar;
    LinearLayout container;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.editTxtEmail);
        inputPassword = (EditText) findViewById(R.id.editTxtPassword);

        btnSignIn = (TextView) findViewById(R.id.btnSignin);
        btnSignUp = (TextView) findViewById(R.id.btnSignup);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        container = (LinearLayout) findViewById(R.id.layoutMainContainer);

        loginPresenter = new LoginPresenterImpl(this);
        loginPresenter.onCreate();
        loginPresenter.checkForAuthentication();

    }

    @Override
    protected void onDestroy() {
        loginPresenter.onDestroy();
        super.onDestroy();
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
        loginPresenter.registerNewUSer(inputEmail.getText().toString()
                                        , inputPassword.getText().toString());
    }

    @Override
    public void handleSignIn(View v) {
        loginPresenter.validateLogin(inputEmail.getText().toString()
                , inputPassword.getText().toString());
    }

    @Override
    public void navigateToMainScreen() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void loginError(String error) {
        inputPassword.setText("");
        String msjError = String.format(getString(R.string.login_error_message_signin), error);
        inputPassword.setError(msjError);
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
        btnSignIn.setEnabled(enabled);
        btnSignUp.setEnabled(enabled);
    }

}
