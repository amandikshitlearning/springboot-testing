package com.example.repository;

import com.example.entity.Student;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName(value = "Student Repository Test Suite")
class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    private Student student;

    @Test
    @DisplayName(value = "Test Save Student")
    void givenStudent_whenSaveStudent_thenReturnSavedStudent() {
//       given - precondition or setup
        student = Student.builder().firstName("John").lastName("Cena").email("john@cena.com").build();
//        when - action or behaviour you want to test
        Student savedStudent = studentRepository.save(student);
//        then - verify output of action/behaviour
        Assertions.assertThat(savedStudent).isNotNull();
        Assertions.assertThat(savedStudent.getFirstName()).isEqualTo(student.getFirstName());
        Assertions.assertThat(savedStudent.getLastName()).isEqualTo(student.getLastName());
        Assertions.assertThat(savedStudent.getEmail()).isEqualTo(student.getEmail());
        Assertions.assertThat(savedStudent.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName(value = "Test Find By ID")
    void givenStudentId_whenFindById_thenReturnStudent() {
//        given - precondition/setup
        Student s1 = Student.builder().firstName("John").lastName("Cena").email("john@cena.com").build();
        studentRepository.save(s1);
//        when - action or behaviour to be tested
        Optional<Student> optionalStudent = studentRepository.findById(s1.getId());
//        then - verify output of action/behaviour tested
        Assertions.assertThat(optionalStudent).isPresent();
        Assertions.assertThat(optionalStudent.get().getId()).isEqualTo(s1.getId());

    }

    @Test
    @DisplayName(value = "Test Find All")
    void givenStudentList_whenFindAll_thenReturnListOfStudents() {
//        given - precondition/setup
        Student s1 = Student.builder().firstName("John").lastName("Cena").email("john@cena.com").build();
        Student s2 = Student.builder().firstName("Roman").lastName("Reigns").email("roman@reigns.com").build();
        List<Student> studentList = Arrays.asList(s1, s2);
        studentRepository.saveAll(studentList);
//        when - action or behaviour to be tested
        List<Student> students = studentRepository.findAll();
//        then - verify output of action/behaviour tested
        Assertions.assertThat(students).hasSize(2);
        Assertions.assertThat(students).isNotEmpty();
    }

    @Test
    @DisplayName(value = "Test Find By Email")
    void givenEmail_whenFindByEmail_thenReturnStudent() {
//        given - precondition/setup
        Student s1 = new Student(1, "Virat", "Kohli", "virat@kohli.com");
        studentRepository.save(s1);
//        when - action or behaviour to be tested
        Student dbStudent = studentRepository.findByEmail(s1.getEmail()).get();
//        then - verify output of action/behaviour tested
        Assertions.assertThat(dbStudent).isNotNull();
        Assertions.assertThat(dbStudent.getEmail()).isEqualTo(s1.getEmail());
    }

    @Test
    @DisplayName(value = "Test Delete By ID")
    void givenStudentId_whenDeleteById_thenDeleteStudent() {
        Student s = new Student(1, "Virat", "Kohli", "virat@kohli.com");
        studentRepository.save(s);
        studentRepository.deleteById(s.getId());
        Optional<Student> optionalStudent = studentRepository.findById(s.getId());
        Assertions.assertThat(optionalStudent).isEmpty();
    }

    @Test
    void givenFirstNameAndLastName_whenFindByJpqlIndexedParams_thenReturnStudent() {
        Student s = new Student(1, "Virat", "Kohli", "virat@kohli.com");
        studentRepository.save(s);
        Student byJpqlIndexedParams = studentRepository.findByJpqlIndexedParams("Virat", "Kohli");
        Assertions.assertThat(byJpqlIndexedParams).isNotNull();
    }

    @Test
    void givenFirstNameAndLastName_whenFindByJpqlNamedParams_thenReturnStudent() {
        Student s = new Student(1, "Virat", "Kohli", "virat@kohli.com");
        studentRepository.save(s);
        Student byJpqlIndexedParams = studentRepository.findByJpqlNamedParams(s.getFirstName(), s.getLastName());
        Assertions.assertThat(byJpqlIndexedParams).isNotNull();
    }

    @Test
    void givenFirstNameAndLastName_whenFindByJpqlNativeQueryIndexedParams_thenReturnStudent() {
        Student s = new Student(1, "Virat", "Kohli", "virat@kohli.com");
        studentRepository.save(s);
        Student byJpqlIndexedParams = studentRepository.findByJpqlNativeQueryIndexedParams(s.getFirstName(), s.getEmail());
        Assertions.assertThat(byJpqlIndexedParams).isNotNull();
    }

    @Test
    void givenFirstNameAndLastName_whenFindByJpqlNativeQueryNamedParams_thenReturnStudent() {
        Student s = new Student(1, "Virat", "Kohli", "virat@kohli.com");
        studentRepository.save(s);
        Student byJpqlIndexedParams = studentRepository.findByJpqlNativeQueryNamedParams(s.getLastName(), s.getEmail());
        Assertions.assertThat(byJpqlIndexedParams).isNotNull();
    }
}