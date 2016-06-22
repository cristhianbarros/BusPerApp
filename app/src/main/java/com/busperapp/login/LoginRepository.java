package com.busperapp.login;

/**
 * Created by cristhian.barros on 15/06/2016.
 */
public interface LoginRepository {

    void signUp(String email, String password);
    void signIn(String email, String password);
    void checkSession();
    void signOut();


}
