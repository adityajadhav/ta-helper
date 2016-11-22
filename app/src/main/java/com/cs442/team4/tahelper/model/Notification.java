package com.cs442.team4.tahelper.model;

/**
 * Created by neo on 21-11-2016.
 */

public class Notification {

    private String body;
    private String title;

    public Notification() {
    }

    public Notification(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
