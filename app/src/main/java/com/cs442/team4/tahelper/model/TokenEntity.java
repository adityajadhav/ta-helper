package com.cs442.team4.tahelper.model;

/**
 * Created by neo on 12-11-2016.
 */

public class TokenEntity {
    private String id;
    private String belongToUser;

    public String getBelongToUser() {
        return belongToUser;
    }

    public void setBelongToUser(String belongToUser) {
        this.belongToUser = belongToUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
