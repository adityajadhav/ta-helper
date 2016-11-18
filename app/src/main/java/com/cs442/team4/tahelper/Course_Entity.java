package com.cs442.team4.tahelper;

/**
 * Created by ullas on 10/29/2016.
 */

public class Course_Entity {

    private String name;
    private String code;
    private String professorFullName;
    private String professorFirstName;
    private String professorLastName;
    private String professorEmailId;
    private String professorUsername;
    private String imported = "false";




    public void setTAEmailIds(String TAEmailIds) {
        this.TAEmailIds = TAEmailIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setProfessorFullName(String professorFullName) {
        this.professorFullName = professorFullName;
    }

    public void setProfessorFirstName(String professorFirstName) {
        this.professorFirstName = professorFirstName;
    }

    public void setProfessorLastName(String professorLastName) {
        this.professorLastName = professorLastName;
    }

    public void setProfessorEmailId(String professorEmailId) {
        this.professorEmailId = professorEmailId;
    }

//    public String getProfessorUsername() {
//        return professorUsername;
//    }

//    public void setProfessorUsername(String professorUsername) {
//        this.professorUsername = professorUsername;
//    }

    //TA email id's seperated by ;
    private String TAEmailIds;


    public Course_Entity(String name, String code, String professor_first_name, String professor_last_name,
                         String professor_email_id, String professor_username, String TA_email_ids, String imported) {
        this.name = name;
        this.code = code;
        this.professorFirstName = professor_first_name;
        this.professorLastName = professor_last_name;
        this.professorEmailId = professor_email_id;

        this.professorFullName = professor_last_name + "," + professor_first_name;
        this.professorUsername = professor_username;
        this.TAEmailIds = TA_email_ids;
        this.imported = imported;
    }

    //Constructor needed for firebsae
    public Course_Entity() {

    }

    public String getImportStatus()
    {
        return imported;
    }

    public String getCourseName() {

        return name;

    }

    public String getCourseCode() {

        return code;

    }

    public String getProfessorFullName() {

        return professorFullName;

    }

    public String getProfessorFirstName() {

        return professorFirstName;

    }

    public String getProfessorLastName() {

        return professorLastName;

    }

    public String getProfessorUserName() {

        return professorUsername;

    }

    public String getProfessorEmailId() {

        return professorEmailId;

    }

    public String getTAEmailIds() {

        return TAEmailIds;

    }

}
