package com.busperapp.entities;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ObjectLost {

    private String key;
    private String title;
    private String description;
    private String address;
    private String category;
    private String postalCode;
    private Map<String, Double> ubicationLatLang;
    private String user;
    private String createdAt;

    public ObjectLost() {

    }

    public ObjectLost(String title, String description, String address, String category, String postalCode, Map<String, Double> ubicationLatLang, String user) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.category = category;
        this.postalCode = postalCode;
        this.ubicationLatLang = ubicationLatLang;
        this.user = user;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ObjectLost{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", category='" + category + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", ubicationLatLang=" + ubicationLatLang +
                ", user='" + user + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("description", description);
        result.put("address", address);
        result.put("category", category);
        result.put("postalCode", postalCode);
        result.put("ubicationLatLang", ubicationLatLang);
        result.put("createdAt", createdAt);
        result.put("key", key);
        result.put("user", user);

        return result;
    }

}
