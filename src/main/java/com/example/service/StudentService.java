package com.example.service;

import com.example.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student saveStudent(Student student);

    List<Student> getAllStudents();
    Optional<Student> getStudentById(long id);

    Student updateStudent(Student student);

    void deleteStudentById(long id);
}
