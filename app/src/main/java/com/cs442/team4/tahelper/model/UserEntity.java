package com.cs442.team4.tahelper.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by neo on 25-10-2016.
 */

public class UserEntity implements Serializable {

    private String id;
    private String name;
    private String username;
    private String email;
    private String givenName;
    private String displayName;
    private String familyName;
    private String photoUrl;

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", givenName='" + givenName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", lastLogedIn='" + lastLogedIn + '\'' +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastLogedIn() {
        return lastLogedIn;
    }

    public void setLastLogedIn() {
        this.lastLogedIn = new Date().toString();
    }

    private String lastLogedIn;

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    /**
     * gets user name
     *
     * @deprecated use display name instead.
     */
    @Deprecated
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @deprecated use id/email id instead.
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserEntity() {
    }

    public UserEntity(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
