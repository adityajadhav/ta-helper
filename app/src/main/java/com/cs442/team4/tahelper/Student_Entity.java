package com.cs442.team4.tahelper;

/**
 * Created by ullas on 10/29/2016.
 */

public class Student_Entity {

    private String username;
    private String first_name;
    private String last_name;
    private String full_name;
    private String email;
    private String a_number;

    public void Student_Entity(String username, String first_name, String last_name, String email, String a_number) {
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.full_name = last_name + "," + first_name;
        this.email = email;
        this.a_number = a_number;
    }

    public String getStudentUserName() {
        return username;
    }

    public String getStudentFirstName() {
        return first_name;
    }

    public String getStudentLastName() {
        return last_name;
    }

    public String getStudentFullName() {
        return full_name;
    }

    public String getStudentEmail() {
        return email;
    }

    public String getStudentANumber() {
        return a_number;
    }
}
