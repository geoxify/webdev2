package com.webdev2;

public class Main {
    public static void main(String[] args) {
        System.out.println("##################################################");
        System.out.println("#      WebDev 2 - Prelim Week 1 Lab Activity     #");
        System.out.println("#       Java for Web Backend Development         #");
        System.out.println("##################################################\n");

        // =================================================================
        // TASK 3: Roster Management with Generics
        // =================================================================
        System.out.println(">>> [TASK 3] Roster Management with Generics <<<");
        Roster roster = new Roster();

        // Adding 7 students with varying ages (some < 18, some >= 18)
        roster.addStudent(new Student("Alice Johnson", 20, "BS Information Technology"));
        roster.addStudent(new Student("Bob Smith", 17, "BS Computer Science"));
        roster.addStudent(new Student("Charlie Davis", 22, "BS Information Systems"));
        roster.addStudent(new Student("Diana Prince", 16, "BS Computer Science"));
        roster.addStudent(new Student("Evan Wright", 19, "BS Information Technology"));
        roster.addStudent(new Student("Fiona Gallagher", 21, "BS Software Engineering"));
        roster.addStudent(new Student("George Miller", 15, "BS Computer Science"));

        System.out.println("Added " + roster.getStudents().size() + " students to the roster.");
        System.out.println("\nFull Student Roster:");
        int index = 1;
        for (Student s : roster.getStudents()) {
            System.out.println(String.format(" %d. %-16s | Age: %2d | Standing: %-30s | Course: %s",
                    index++, s.getName(), s.getAge(), s.computeStanding(), s.getCourse()));
        }
        System.out.println();

        // =================================================================
        // TASK 4: Exception Handling for Invalid Data
        // =================================================================
        System.out.println(">>> [TASK 4] Exception Handling for Invalid Data <<<");

        // Test 1: Invalid student instantiation with negative age
        try {
            System.out.println("Attempting to instantiate Student with negative age (-5)...");
            Student invalidStudent = new Student("Invalid Student", -5, "BS Information Technology");
            roster.addStudent(invalidStudent);
        } catch (InvalidAgeException ex) {
            System.out.println(" -> [CAUGHT EXCEPTION] " + ex.getClass().getName() + ": " + ex.getMessage());
            System.out.println(" -> Handled successfully. Program continues execution without crashing.");
        }

        // Test 2: Invalid setter call on existing student
        try {
            System.out.println("\nAttempting to update an existing student's age to -1 via setter...");
            Student student = roster.getStudents().get(0);
            student.setAge(-1);
        } catch (InvalidAgeException ex) {
            System.out.println(" -> [CAUGHT EXCEPTION] " + ex.getClass().getName() + ": " + ex.getMessage());
            System.out.println(" -> Handled successfully. Program continues execution without crashing.");
        }

        System.out.println("\n[SUCCESS] Execution resumed normally after catching InvalidAgeException.\n");

        // =================================================================
        // TASK 5: Stream-Based Roster Report
        // =================================================================
        System.out.println(">>> [TASK 5] Stream-Based Roster Report <<<");
        roster.generateAdultStudentsReport();

        System.out.println("\n##################################################");
        System.out.println("#             ALL TASKS COMPLETE                 #");
        System.out.println("##################################################");
    }
}
