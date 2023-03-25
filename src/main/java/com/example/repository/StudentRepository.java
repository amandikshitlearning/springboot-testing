package com.example.repository;

import com.example.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    @Query(value = "select s from Student s where s.firstName=?1 and s.lastName=?2")
    Student findByJpqlIndexedParams(String firstName, String lastName);

    @Query(value = "select s from Student s where s.firstName=:firstName and s.lastName=:lastName")
    Student findByJpqlNamedParams(String firstName, String lastName);

    @Query(value = "select * from students where first_name=?1 and email=?2", nativeQuery = true)
    Student findByJpqlNativeQueryIndexedParams(String firstName, String email);

    @Query(value = "select * from students where last_name=:lastName and email=:email", nativeQuery = true)
    Student findByJpqlNativeQueryNamedParams(String lastName, String email);

}
