package com.cs442.team4.tahelper.student.groups;

import com.cs442.team4.tahelper.student.Student_Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Aditya on 06-11-2016.
 */

public class LexicographicStudentGroupsImpl implements StudentGroups<Student_Entity> {

    @Override
    public List<List<Student_Entity>> generateGroups(List<Student_Entity> students, int grpSize) {

        Collections.sort((List<Student_Entity>) students, new LexicographicComparator());
        return generateGroup(students, grpSize);
    }

    private static <T> List<List<T>> generateGroup(List<T> students, int grpSize) {
        List<List<T>> lists = new ArrayList();
        Iterator<T> i = students.iterator();
        while (i.hasNext()) {
            List<T> list = new ArrayList();
            for (int j = 0; i.hasNext() && j < grpSize; j++) {
                list.add(i.next());
            }
            lists.add(list);
        }
        return lists;
    }
}
