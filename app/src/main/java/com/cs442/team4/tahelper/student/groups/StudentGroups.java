package com.cs442.team4.tahelper.student.groups;

import java.util.Collection;

/**
 * Created by Aditya on 06-11-2016.
 */

public interface StudentGroups<T> {

    public Collection<Collection<T>> generateGroups(Collection<T> students, int grpSize);

}
