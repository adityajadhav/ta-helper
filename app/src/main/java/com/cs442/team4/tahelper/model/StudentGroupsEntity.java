package com.cs442.team4.tahelper.model;

import com.cs442.team4.tahelper.student.Student_Entity;

import java.util.List;

/**
 * Created by neo on 05-11-2016.
 */

public class StudentGroupsEntity {

    private List<Student_Entity> groups;

    public StudentGroupsEntity(int groupSize) {

    }

    public List<Student_Entity> getGroups() {
        return groups;
    }

    public void setGroups(List<Student_Entity> groups) {
        this.groups = groups;
    }
}
