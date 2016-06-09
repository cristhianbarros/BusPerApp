package com.busperapp.login;

/**
 * Created by cristhian.barros on 15/06/2016.
 */
public class LoginPresenterImpl implements LoginPresenter {

    private LoginView loginView;
    private LoginInteractor loginInteractor;


    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.loginInteractor = new LoginInteractorImpl();
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void checkForAuthentication() {
        if(loginView != null) {
            loginView.disableInputs();
            loginView.showProgressBar();
        }

        loginInteractor.checkSession();
    }

    @Override
    public void validateLogin(String email, String password) {
        if(loginView != null) {
            loginView.disableInputs();
            loginView.showProgressBar();
        }

        loginInteractor.doSignIn(email, password);
    }

    @Override
    public void registerNewUSer(String email, String password) {
        if(loginView != null) {
            loginView.disableInputs();
            loginView.showProgressBar();
        }

        loginInteractor.doSignUp(email, password);

    }

    private void onSignInSuccess() {
        if(loginView != null) {
            loginView.navigateToMainScreen();
        }
    }

    private void onSignUpSuccess() {
        if(loginView != null) {
            loginView.newUserSuccess();
        }

    }

    private void onSignInError(String error) {
        if(loginView != null) {
            loginView.hideProgressBar();
            loginView.enableInputs();
            loginView.loginError(error);
        }
    }

    private void onSignUpError(String error) {
        if(loginView != null) {
            loginView.hideProgressBar();
            loginView.enableInputs();
            loginView.newUSerError(error);
        }
    }

}
