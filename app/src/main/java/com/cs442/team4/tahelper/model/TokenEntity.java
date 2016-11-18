package com.cs442.team4.tahelper.model;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by neo on 12-11-2016.
 */

public class TokenEntity {

    private String id;
    private String belongToUser;
    private String createdDate;

    public TokenEntity() {
        this.createdDate = DateFormat.getDateTimeInstance().format(new Date());
    }

    public String getBelongToUser() {
        return belongToUser;
    }

    public void setBelongToUser(String belongToUser) {
        this.belongToUser = belongToUser;
    }

    public String getId() {
        return id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setId(String id) {
        this.id = id;
    }

}
