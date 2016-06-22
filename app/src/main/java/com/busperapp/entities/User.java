package com.busperapp.entities;

/**
 * Created by cristhian.barros on 18/06/2016.
 */
public class User {

    private String email;
    private boolean online;

    public final static boolean ONLINE = true;
    public final static boolean OFFLINE = false;

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
