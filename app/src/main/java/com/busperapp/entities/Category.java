package com.busperapp.entities;

/**
 * Created by cristhian.barros on 06/06/2016.
 */
public class Category {

    private String name;
    private String icon;
    private Boolean active;

    public static final String FIREBASE_TAG = "categories";

    public Category() {

    }

    public Category(String name, String icon, Boolean active) {
        this.name = name;
        this.icon = icon;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return name;
    }
}
