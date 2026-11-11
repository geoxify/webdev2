package com.webdev2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages a collection of Student objects using generics and Java Streams.
 */
public class Roster {
    private final List<Student> students;

    /**
     * Initializes an empty student roster.
     */
    public Roster() {
        this.students = new ArrayList<>();
    }

    /**
     * Adds a student to the roster.
     *
     * @param student The student to add.
     */
    public void addStudent(Student student) {
        if (student != null) {
            this.students.add(student);
        }
    }

    /**
     * Returns an unmodifiable view of all students in the roster.
     *
     * @return List of students.
     */
    public List<Student> getStudents() {
        return Collections.unmodifiableList(this.students);
    }

    /**
     * Task 5: Stream-Based Roster Report.
     * Uses Java streams to:
     *  (a) filter students who are 18 or older
     *  (b) map the result to a list of names only
     *  (c) print a count of how many students qualified
     *
     * @return List of names of students aged 18 or older.
     */
    public List<String> generateAdultStudentsReport() {
        // Stream pipeline: filter -> map -> collect
        List<String> adultStudentNames = this.students.stream()
                .filter(student -> student.getAge() >= 18)
                .map(Student::getName)
                .collect(Collectors.toList());

        // Count using terminal stream operation or list size
        long count = this.students.stream()
                .filter(student -> student.getAge() >= 18)
                .count();

        // Print report output
        System.out.println("==================================================");
        System.out.println("        STREAM REPORT: STUDENTS AGED 18+          ");
        System.out.println("==================================================");
        System.out.println("Qualified Student Names (18 or older):");
        for (String name : adultStudentNames) {
            System.out.println("  - " + name);
        }
        System.out.println("--------------------------------------------------");
        System.out.println("Count of qualified students (age >= 18): " + count);
        System.out.println("==================================================");

        return adultStudentNames;
    }
}
