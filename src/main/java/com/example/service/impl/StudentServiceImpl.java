package com.example.service.impl;

import com.example.entity.Student;
import com.example.exception.ResourceAlreadyExistsException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.StudentRepository;
import com.example.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student saveStudent(Student student) {
        Optional<Student> optionalStudent = studentRepository.findByEmail(student.getEmail());
        if (optionalStudent.isPresent()) {
            throw new ResourceAlreadyExistsException("Student with given email already exists " + student.getEmail());
        }
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        log.info("Get All Students");
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getStudentById(long id) {
        return studentRepository.findById(id);
    }

    @Override
    public Student updateStudent(Student student) {
        Optional<Student> optionalStudent = studentRepository.findById(student.getId());
        if (!optionalStudent.isPresent()) {
            throw new ResourceNotFoundException("No Student found with Id " + student.getId());
        }
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudentById(long id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (!optionalStudent.isPresent()) {
            throw new ResourceNotFoundException("No Student found with Id " + id);
        }
        studentRepository.deleteById(id);
    }
}
