package com.webdev2;

/**
 * Encapsulated domain class representing a Student.
 * Implements Gradable to determine academic standing.
 */
public class Student implements Gradable {
    private String name;
    private int age;
    private String course;

    /**
     * Parameterized constructor to initialize a Student instance.
     *
     * @param name   The name of the student.
     * @param age    The age of the student (must be non-negative).
     * @param course The enrolled course of the student.
     */
    public Student(String name, int age, String course) {
        this.name = name;
        setAge(age); // Enforces age validation through setter
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    /**
     * Sets the student's age. Rejects negative values by throwing InvalidAgeException.
     *
     * @param age The age to set.
     * @throws InvalidAgeException if age is negative.
     */
    public void setAge(int age) {
        if (age < 0) {
            throw new InvalidAgeException("Invalid age: " + age + ". Age cannot be negative.");
        }
        this.age = age;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * Computes the academic standing of the student.
     *
     * @return Standing as a String description.
     */
    @Override
    public String computeStanding() {
        if (this.age >= 18) {
            return "Good Standing (Regular)";
        } else {
            return "Good Standing (Minor / Early College)";
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", course='" + course + '\'' +
                ", standing='" + computeStanding() + '\'' +
                '}';
    }
}
