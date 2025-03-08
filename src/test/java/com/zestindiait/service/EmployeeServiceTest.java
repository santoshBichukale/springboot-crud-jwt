package com.zestindiait.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.zestindiait.entity.Employee;
import com.zestindiait.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee(1L, "John Doe", "john.doe@example.com", "IT", "Software Engineer", 60000.0, "2024-01-01");
    }

    @Test
    void testSaveEmployee() {
        when(employeeRepository.save(employee)).thenReturn(employee);
        Employee savedEmployee = employeeService.saveEmployee(employee);
        assertNotNull(savedEmployee);
        assertEquals("John Doe", savedEmployee.getName());
    }

    @Test
    void testGetAllEmployees() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> page = new PageImpl<>(List.of(employee));
        when(employeeRepository.findAll(pageable)).thenReturn(page);
        
        Page<Employee> result = employeeService.getAllEmployees(pageable);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        Optional<Employee> foundEmployee = employeeService.getEmployeeById(1L);
        assertTrue(foundEmployee.isPresent());
        assertEquals("John Doe", foundEmployee.get().getName());
    }

    @Test
    void testUpdateEmployee() {
        when(employeeRepository.save(employee)).thenReturn(employee);
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        assertNotNull(updatedEmployee);
        assertEquals("John Doe", updatedEmployee.getName());
    }

    @Test
    void testDeleteEmployeeById() {
        when(employeeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(1L);
        boolean isDeleted = employeeService.deleteEmployeeById(1L);
        assertTrue(isDeleted);
    }

    @Test
    void testDeleteEmployeeById_NotFound() {
        when(employeeRepository.existsById(2L)).thenReturn(false);
        boolean isDeleted = employeeService.deleteEmployeeById(2L);
        assertFalse(isDeleted);
    }
}