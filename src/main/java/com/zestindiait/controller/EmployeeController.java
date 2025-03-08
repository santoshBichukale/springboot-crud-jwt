package com.zestindiait.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.zestindiait.entity.Employee;
import com.zestindiait.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")

public class EmployeeController {

	@Autowired
	EmployeeService employeeService;


	@PostMapping("/register-employee")
	public ResponseEntity<String> registerEmployee(@RequestBody Employee employee) {
		Employee savedEmployee = employeeService.saveEmployee(employee);
		if (savedEmployee != null) {
			return new ResponseEntity<>("Employee added successfully", HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Employee already exists", HttpStatus.BAD_REQUEST);
	}

	@GetMapping
	public ResponseEntity<Page<Employee>> getAllEmployees(Pageable pageable) {
		Page<Employee> employees = employeeService.getAllEmployees(pageable);
		if (employees.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	@PutMapping("/update-employee")
	public ResponseEntity<Employee> updateEmployee(@RequestBody Employee emp) {
		Employee updatedEmployee = employeeService.updateEmployee(emp);
		if (updatedEmployee != null) {
			return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping
	public ResponseEntity<String> deleteEmployeeById(@RequestParam long id) {
		boolean status = employeeService.deleteEmployeeById(id);
		if (status) {
			return new ResponseEntity<>("Employee deleted successfully", HttpStatus.OK);
		}
		return new ResponseEntity<>("Employee not found", HttpStatus.BAD_REQUEST);
	}
	
}
