package com.busperapp.login;

/**
 * Created by cristhian.barros on 15/06/2016.
 */
public interface LoginPresenter {

    void onDestroy();

    void checkForAuthentication();
    void validateLogin(String email, String password);
    void registerNewUSer(String email, String password);


}
