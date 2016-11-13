package com.cs442.team4.tahelper.student;

/**
 * Created by ullas on 10/29/2016.
 */

public class Student_Entity {

    public Student_Entity(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    private String userName;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String aNumber;
    private int belongToGroup;

    public int getBelongToGroup() {
        return belongToGroup;
    }

    public void setBelongToGroup(int belongToGroup) {
        this.belongToGroup = belongToGroup;
    }


    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Student_Entity() {
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getaNumber() {
        return aNumber;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setaNumber(String aNumber) {
        this.aNumber = aNumber;
    }



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

    public String getDisplayText() {
        String groupId = getBelongToGroup() == 0 ? "" : (" Group " + getBelongToGroup());
        return firstName + " " + lastName + groupId;
    }


    @Override
    public String toString() {
        return lastName;
        /*return "Student_Entity{" +
                "userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", aNumber='" + aNumber + '\'' +
                '}';*/
    }
}
