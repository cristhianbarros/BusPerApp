package com.busperapp.model;

import java.util.Map;
import java.util.UUID;

public class ObjectLost {

    private String title;
    private String description;
    private String address;
    private String category;
    private String postalCode;
    private Map<String, Double> ubicationLatLang;

    public static final String FIREBASE_TAG = "object_lost";


    public ObjectLost() {

    }

    public ObjectLost(String title, String description, String address, String category, String postalCode, Map<String, Double> ubicationLatLang) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.category = category;
        this.postalCode = postalCode;
        this.ubicationLatLang = ubicationLatLang;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Map<String, Double> getUbicationLatLang() {
        return ubicationLatLang;
    }

    public void setUbicationLatLang(Map<String, Double> ubicationLatLang) {
        this.ubicationLatLang = ubicationLatLang;
    }

}
