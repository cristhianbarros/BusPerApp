package com.busperapp.login;

import com.busperapp.login.events.LoginEvent;

/**
 * Created by cristhian.barros on 15/06/2016.
 */
public interface LoginPresenter {

    void onCreate();
    void onDestroy();

    void checkForAuthentication();
    void validateLogin(String email, String password);
    void registerNewUSer(String email, String password);
    void onEventMainThread(LoginEvent event);

}
