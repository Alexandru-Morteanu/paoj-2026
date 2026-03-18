package com.pao.laboratory03.exercise.service;

import com.pao.laboratory03.exercise.exception.StudentNotFoundException;
import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.model.Subject;

import java.util.*;

public class StudentService {
    private static StudentService instance;
    private final List<Student> students;

    private StudentService() {
        this.students = new ArrayList<>();
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public void addStudent(String name, int age) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                throw new RuntimeException("Studentul cu numele " + name + " există deja.");
            }
        }
        students.add(new Student(name, age));
    }

    public Student findByName(String name) {
        return students.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new StudentNotFoundException("Studentul " + name + " nu a fost găsit."));
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        Student student = findByName(studentName);
        student.addGrade(subject, grade);
    }

    public void printAllStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu există studenți în sistem.");
            return;
        }
        students.forEach(s -> {
            System.out.println(s);
            s.getGrades().forEach((sub, grade) -> System.out.println("  - " + sub.name() + ": " + grade));
        });
    }

    public void printTopStudents() {
        students.stream()
                .sorted((s1, s2) -> Double.compare(s2.getAverage(), s1.getAverage()))
                .forEach(System.out::println);
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> sums = new HashMap<>();
        Map<Subject, Integer> counts = new HashMap<>();

        for (Student s : students) {
            s.getGrades().forEach((subject, grade) -> {
                sums.put(subject, sums.getOrDefault(subject, 0.0) + grade);
                counts.put(subject, counts.getOrDefault(subject, 0) + 1);
            });
        }

        Map<Subject, Double> averages = new HashMap<>();
        sums.forEach((subject, sum) -> averages.put(subject, sum / counts.get(subject)));
        return averages;
    }
}