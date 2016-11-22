package com.cs442.team4.tahelper.model;

/**
 * Created by neo on 21-11-2016.
 */

public class PushNotification {

    private String to;
    private Notification notification;

    public PushNotification() {

    }

    public PushNotification(String to, String title, String body) {
        this.to = to;
        this.notification = new Notification(title, body);
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


}


