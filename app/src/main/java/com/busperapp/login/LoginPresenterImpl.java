package com.busperapp.login;

import com.busperapp.login.events.LoginEvent;
import com.busperapp.login.ui.LoginView;
import com.busperapp.util.EventBus;
import com.busperapp.util.GreenRobotEventBus;

import org.greenrobot.eventbus.Subscribe;

public class LoginPresenterImpl implements LoginPresenter {
    private EventBus eventBus;
    private LoginView loginView;
    private LoginInteractor loginInteractor;


    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.loginInteractor = new LoginInteractorImpl();
        this.eventBus = GreenRobotEventBus.getInstance();
    }


    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        loginView = null;
        eventBus.unregister(this);
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

    @Override
    @Subscribe
    public void onEventMainThread(LoginEvent event) {

        switch(event.getEventType()) {
            case LoginEvent.onSignInSuccess:
                onSignInSuccess();
                break;
            case LoginEvent.onSignInError:
                onSignInError(event.getErrorMessage());
                break;
            case LoginEvent.onSignUpSuccess:
                onSignUpSuccess();
                break;
            case LoginEvent.onSignUpError:
                onSignUpError(event.getErrorMessage());
                break;
            case LoginEvent.onFailedRecoverSession:
                onFailedToRecoverSession();
                break;

        }
    }

    public void onFailedToRecoverSession() {
        if(loginView != null) {
            loginView.hideProgressBar();
            loginView.enableInputs();
        }
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
