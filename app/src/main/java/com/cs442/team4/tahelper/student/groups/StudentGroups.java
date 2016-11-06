package com.cs442.team4.tahelper.student.groups;

import java.util.List;

/**
 * Created by Aditya on 06-11-2016.
 */

public interface StudentGroups<T> {

    public List<List<T>> generateGroups(List<T> students, int grpSize);

}
