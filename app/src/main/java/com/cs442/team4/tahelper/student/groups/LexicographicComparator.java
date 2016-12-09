package com.cs442.team4.tahelper.student.groups;

import com.cs442.team4.tahelper.student.Student_Entity;

import java.util.Comparator;

/**
 * Created by neo on 06-11-2016.
 * Student Comparator
 */
public class LexicographicComparator implements Comparator<Student_Entity> {

    @Override
    public int compare(Student_Entity s1, Student_Entity s2) {
       // if (!(s1.getStudentLastName().equalsIgnoreCase(s2.getStudentLastName())))
            return s1.getStudentLastName().compareToIgnoreCase(s2.getStudentLastName());
       // else
       //     return s2.getStudentFirstName().compareTo(s1.getStudentFirstName());
    }
}
