package com.busperapp.login;

/**
 * Created by cristhian.barros on 15/06/2016.
 */
public interface LoginInteractor {

    void checkSession();
    void doSignUp(String email, String password);
    void doSignIn(String email, String password);
    void doSignOut();

}
