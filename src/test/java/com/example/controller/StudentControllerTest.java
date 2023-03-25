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

}