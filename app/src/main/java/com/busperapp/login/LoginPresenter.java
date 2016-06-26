package com.busperapp.login;

import com.busperapp.login.events.LoginEvent;

public interface LoginPresenter {

    void onCreate();
    void onDestroy();

    void checkForAuthentication();
    void validateLogin(String email, String password);
    void registerNewUser(String email, String password);
    void registerNewUser(String email, String password, String names, String surnames);
    void onEventMainThread(LoginEvent event);

}
