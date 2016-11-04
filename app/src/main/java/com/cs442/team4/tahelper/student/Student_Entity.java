package com.cs442.team4.tahelper.student;

/**
 * Created by ullas on 10/29/2016.
 */

public class Student_Entity {

    private String userName;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String aNumber;

    public Student_Entity(String username, String first_name, String last_name, String email, String a_number) {
        this.userName = username;
        this.firstName = first_name;
        this.lastName = last_name;
        this.fullName = last_name + "," + first_name;
        this.email = email;
        this.aNumber = a_number;
    }

    public String getStudentUserName() {
        return userName;
    }

    public String getStudentFirstName() {
        return firstName;
    }

    public String getStudentLastName() {
        return lastName;
    }

    public String getStudentFullName() {
        return fullName;
    }

    public String getStudentEmail() {
        return email;
    }

    public String getStudentANumber() {
        return aNumber;
    }
}
