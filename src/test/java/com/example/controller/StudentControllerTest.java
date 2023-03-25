package com.example.controller;

import com.example.entity.Student;
import com.example.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebMvcTest(StudentController.class)
@DisplayName(value = "JUnit Test Case suite for Student Controller")
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName(value = "Test Create Student REST API")
    public void givenStudent_whenCreateStudent_thenReturnSavedStudent() throws Exception {
//        given - precondition/setup
        Student student = Student.builder().firstName("john").lastName("cena").email("john@cena.com").build();
        BDDMockito.given(studentService.saveStudent(ArgumentMatchers.any(Student.class))).willAnswer(invocation -> invocation.getArgument(0));
//        when - action to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/student")
                                                                       .contentType(MediaType.APPLICATION_JSON)
                                                                       .content(objectMapper.writeValueAsString(student)));
//        then - verify output of action
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(student.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(student.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(student.getEmail())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void givenListOfStudents_whenGetAllStudents_thenReturnStudentsList() throws Exception {
        //        given - precondition/setup
        Student s1 = Student.builder().firstName("john").lastName("cena").email("john@cena.com").build();
        Student s2 = Student.builder().firstName("roman").lastName("reigns").email("roman@reigns.com").build();
        Student s3 = Student.builder().firstName("seth").lastName("rollins").email("seth@rollins.com").build();
        List<Student> studentList = Arrays.asList(s1, s2, s3);
        BDDMockito.given(studentService.getAllStudents()).willReturn(studentList);
        //        when - action/behaviour you want to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/student"));
        //        then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(studentList.size())));
    }


    //    Junit test for Get Student By Id REST API
    @Test
    @DisplayName(value = "Junit test for Get Student By Id REST API - Valid Employee ID")
    void givenStudentID_whenGetStudentByID_thenReturnStudentObject() throws Exception{
        //        given - precondition/setup
        long studentId = 1l;
        Student s1 = Student.builder().firstName("john").lastName("cena").email("john@cena.com").build();
        BDDMockito.given(studentService.getStudentById(studentId)).willReturn(Optional.of(s1));
        //        when - action/behaviour you want to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/student/{id}", studentId));

        //        then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(s1.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(s1.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(s1.getEmail())));
    }

    //    Junit test for
    @Test
    void givenStudentId_whenGetStudentById_thenReturnNotFound() throws Exception{
        //        given - precondition/setup
        long studentId = 1l;
        BDDMockito.given(studentService.getStudentById(studentId)).willReturn(Optional.empty());

        //        when - action/behaviour you want to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/student/{id}", studentId));

        //        then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
}