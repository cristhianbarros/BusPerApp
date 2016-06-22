package com.busperapp.entities;


import java.util.UUID;

public class PostalCode {

    private String _id;
    private String codePostal;
    private String stateName;
    private String stateCode;
    private String cityName;
    private String cityCode;
    private String suburbName;
    private String suburbCode;

    public PostalCode() {

    }

    public PostalCode(String codePostal, String stateName, String stateCode, String cityName, String cityCode, String suburbName, String suburbCode) {
        this._id = UUID.randomUUID().toString();
        this.codePostal = codePostal;
        this.stateName = stateName;
        this.stateCode = stateCode;
        this.cityName = cityName;
        this.cityCode = cityCode;
        this.suburbName = suburbName;
        this.suburbCode = suburbCode;
    }

    public String get_id() {
        return _id;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getSuburbName() {
        return suburbName;
    }

    public void setSuburbName(String suburbName) {
        this.suburbName = suburbName;
    }

    public String getSuburbCode() {
        return suburbCode;
    }

    public void setSuburbCode(String suburbCode) {
        this.suburbCode = suburbCode;
    }

    @Override
    public String toString() {
        return getStateName().substring(0,3)+" - "+getCityName().substring(0,3)+" - "+getSuburbName();
    }
}

