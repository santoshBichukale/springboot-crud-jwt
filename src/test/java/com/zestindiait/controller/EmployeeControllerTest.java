package com.zestindiait.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zestindiait.entity.Employee;
import com.zestindiait.security.JwtHelper;
import com.zestindiait.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.util.Arrays;

@WebMvcTest(EmployeeController.class)

public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;
    
    @MockBean
    private JwtHelper jwtHelper;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setEmail("johndoe@example.com");
        employee.setDepartment("IT");
        employee.setPosition("Software Engineer");
        employee.setSalary(75000);
        employee.setDateOfJoining("20/3/25");
    }

    @Test
    void testRegisterEmployee() throws Exception {
        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/employees/register-employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Employee added successfully"));
    }

    @Test
    void testGetAllEmployees() throws Exception {
        Page<Employee> employeePage = new PageImpl<>(Arrays.asList(employee));
        when(employeeService.getAllEmployees(any(Pageable.class))).thenReturn(employeePage);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("John Doe"))
                .andExpect(jsonPath("$.content[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$.content[0].department").value("IT"));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        when(employeeService.updateEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(put("/api/employees/update-employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@example.com"));
    }

    @Test
    void testUpdateEmployeeNotFound() throws Exception {
        when(employeeService.updateEmployee(any(Employee.class))).thenReturn(null);

        mockMvc.perform(put("/api/employees/update-employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteEmployeeById() throws Exception {
        when(employeeService.deleteEmployeeById(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/employees")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully"));
    }

    @Test
    void testDeleteEmployeeNotFound() throws Exception {
        when(employeeService.deleteEmployeeById(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/api/employees")
                .param("id", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Employee not found"));
    }
}
