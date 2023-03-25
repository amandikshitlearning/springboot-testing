package com.example.service.impl;

import com.example.entity.Student;
import com.example.exception.ResourceAlreadyExistsException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mockingDetails;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = Student.builder().firstName("john").lastName("cena").email("john@cena.com").build();
    }

    @Test
    @DisplayName(value = "JUnit Test Case for Save Student")
    void givenStudent_whenSaveStudent_thenReturnSavedStudent() {
//        given - precondition or setup
        student.setId(1);
        given(studentRepository.findByEmail(student.getEmail())).willReturn(Optional.empty());
        given(studentRepository.save(student)).willReturn(student);
//        when - action or behaviour to be tested
        Student savedStudent = studentService.saveStudent(student);
//        then - verify result of action/behaviour tested
        verify(studentRepository, atLeastOnce()).findByEmail(student.getEmail());
        verify(studentRepository, atMost(1)).save(student);
        Assertions.assertThat(savedStudent).isNotNull();
        System.out.println(mockingDetails(studentRepository).printInvocations());
    }

    @Test
    @DisplayName(value = "JUnit Test Case for Save Student with duplicate email")
    void givenExistingEmail_whenSaveStudent_thenThrowException() {
        //        given - precondition/setup
        given(studentRepository.findByEmail(student.getEmail())).willReturn(Optional.of(student));
        //        when - action/behaviour you want to test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> {
            studentService.saveStudent(student);
        });
//        then - verify output of action
        verify(studentRepository, never()).save(any(Student.class));
    }

    //    Junit test for
    @Test
    @DisplayName(value = "Junit Test Case for Get All Students")
    void givenStudentList_whenGetAllStudents_thenReturnListOfStudents() {
        //        given - precondition/setup

        Student s2 = Student.builder().firstName("roman").lastName("reigns").email("roman@reigns.com").build();
        Student s3 = Student.builder().firstName("seth").lastName("rollins").email("seth@rollins.com").build();
        List<Student> students = Arrays.asList(student, s2, s3);
        given(studentRepository.findAll()).willReturn(students);
        //        when - action/behaviour you want to test
        List<Student> allStudents = studentService.getAllStudents();
        //        then - verify the output
        verify(studentRepository, times(1)).findAll();
        Assertions.assertThat(allStudents).isNotEmpty();
        Assertions.assertThat(allStudents.size()).isEqualTo(3);
        System.out.println(mockingDetails(studentRepository).printInvocations());
    }

    //    Junit test for
    @Test
    @DisplayName(value = "JUnit Test Case for Get All for Empty List")
    void givenEmptyStudentList_whenFindAll_thenReturnEmptyStudentList() {
        //        given - precondition/setup
        ArrayList<Student> students = new ArrayList<>();
        given(studentRepository.findAll()).willReturn(students);
        //        when - action/behaviour you want to test
        List<Student> allStudents = studentService.getAllStudents();
        //        then - verify the output
        verify(studentRepository, atMostOnce()).findAll();
        Assertions.assertThat(allStudents).isEmpty();
    }

    //    Junit test for
    @Test
    void givenStudentId_whenGetStudentById_thenReturnStudent() {
        //        given - precondition/setup
        given(studentRepository.findById(anyLong())).willReturn(Optional.of(student));
        //        when - action/behaviour you want to test
        Optional<Student> studentById = studentService.getStudentById(student.getId());
        //        then - verify the output
        verify(studentRepository, atMostOnce()).findById(student.getId());
        Assertions.assertThat(studentById).isPresent();
        Assertions.assertThat(studentById.get().getFirstName()).isEqualTo(student.getFirstName());
        System.out.println(mockingDetails(studentRepository).printInvocations());
    }

    @Test
    @DisplayName(value = "JUnit Test Case for Update Student")
    void givenStudent_whenUpdateStudent_thenReturnUpdatedStudent() {
        //        given - precondition/setup
        given(studentRepository.findById(student.getId())).willReturn(Optional.of(student));
        given(studentRepository.save(student)).willReturn(student);
        //        when - action/behaviour you want to test
        Student updateStudent = studentService.updateStudent(student);
        //        then - verify the output
        verify(studentRepository, atLeastOnce()).save(student);
        Assertions.assertThat(updateStudent).isNotNull();
        System.out.println(mockingDetails(studentRepository).printInvocations());
    }

    @Test
    @DisplayName(value = "JUnit Test Case for Update Student Negative Scenario")
    void givenNonExistingStudent_whenUpdateStudent_thenThrowException() {
        //        given - precondition/setup
        given(studentRepository.findById(student.getId())).willReturn(Optional.empty());
        //        when - action/behaviour you want to test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            studentService.updateStudent(student);
        });
        //        then - verify the output
        verify(studentRepository, never()).save(student);
        System.out.println(mockingDetails(studentRepository).printInvocations());
    }

    @Test
    void givenId_whenDeleteStudentById_thenDeleteStudent() {
        //        given - precondition/setup
            given(studentRepository.findById(student.getId())).willReturn(Optional.of(student));
        //        when - action/behaviour you want to test

        //        then - verify the output
    }
}