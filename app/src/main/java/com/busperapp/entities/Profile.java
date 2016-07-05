package com.busperapp.entities;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Profile {

    private String cellPhone;
    private Boolean isPublic;
    private String email;
    private String names;
    private String surnames;


    public Profile() {

    }


    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "cellPhone='" + cellPhone + '\'' +
                ", isPublic=" + isPublic +
                ", email='" + email + '\'' +
                ", names='" + names + '\'' +
                ", surnames='" + surnames + '\'' +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cellPhone", cellPhone);
        result.put("public", isPublic);
        result.put("email", email);
        result.put("names", names);
        result.put("surnames", surnames);

        return result;
    }
}
