package com.cs442.team4.tahelper;

import com.cs442.team4.tahelper.student.Student_Entity;
import com.cs442.team4.tahelper.student.groups.LexicographicStudentGroupsImpl;

import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

/**
 * Created by neo on 06-11-2016.
 */

public class StudentsUnitTest {

    @Test
    public void generate_groups() throws Exception {
        LexicographicStudentGroupsImpl impl = new LexicographicStudentGroupsImpl();
        Collection<?> list = impl.generateGroups(getStudents(), 3);
        System.out.println(list);
        assertTrue(true);
    }


    private List<Student_Entity> getStudents() {
        Student_Entity a = new Student_Entity("a", "a", "a", "a", "a");
        Student_Entity c = new Student_Entity("c", "c", "c", "c", "c");
        Student_Entity d = new Student_Entity("d", "d", "d", "d", "d");
        Student_Entity e = new Student_Entity("e", "e", "e", "e", "e");
        Student_Entity b = new Student_Entity("b", "b", "b", "b", "b");
        return asList(a, b, c, d, e);
    }

}
