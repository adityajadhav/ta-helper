package com.cs442.team4.tahelper;

/**
 * Created by ullas on 10/29/2016.
 */

public class Course_Entity {

    private String name;
    private String code;
    private String professor_full_name;
    private String professor_first_name;
    private String professor_last_name;
    private String professor_email_id;
    private String professor_username;

    //TA email id's seperated by ;
    private String TA_email_ids;

    public void Course_Entity(String name, String code, String professor_first_name, String professor_last_name,
                              String professor_email_id, String professor_username, String TA_email_ids) {
        this.name = name;
        this.code = code;
        this.professor_first_name = professor_first_name;
        this.professor_last_name = professor_last_name;
        this.professor_email_id = professor_email_id;
        this.professor_username = professor_username;
        this.professor_full_name = professor_last_name + "," + professor_first_name;
        this.TA_email_ids = TA_email_ids;
    }


    public String getCourseName() {

        return name;

    }

    public String getCourseCode() {

        return code;

    }

    public String getProfessorFullName() {

        return professor_full_name;

    }

    public String getProfessorFirstName() {

        return professor_first_name;

    }

    public String getProfessorLastName() {

        return professor_last_name;

    }

    public String getProfessorUserName() {

        return professor_username;

    }

    public String getProfessorEmailId() {

        return professor_email_id;

    }

    public String getTAEmailIds() {

        return TA_email_ids;

    }

}
