package com.busperapp.login;

/**
 * Created by cristhian.barros on 15/06/2016.
 */
public class LoginInteractorImpl  implements  LoginInteractor{

    private LoginRepository loginRepository;

    public LoginInteractorImpl() {
        loginRepository = new LoginRepositoryImpl();
    }

    @Override
    public void checkSession() {
        loginRepository.checkSession();
    }

    @Override
    public void doSignUp(String email, String password) {
        loginRepository.signUp(email, password);
    }

    @Override
    public void doSignUp(String email, String password, String names, String surNames) {
        loginRepository.signUp(email, password, names, surNames);
    }

    @Override
    public void doSignIn(String email, String password) {
        loginRepository.signIn(email, password);
    }

    @Override
    public void doSignOut() {
        loginRepository.signOut();
    }
}
