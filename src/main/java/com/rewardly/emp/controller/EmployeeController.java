package com.rewardly.emp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rewardly.emp.entity.Employee;
import com.rewardly.emp.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

	private final EmployeeService employeeService;

	@PostMapping
	public ResponseEntity<String> createEmployee(@RequestBody Employee employee) {

		return ResponseEntity.ok(employeeService.createEmployee(employee));
	}

	@GetMapping("/hello")
	public String hello() {
		return "Hello from Employee Service";
	}

	@GetMapping("/{id}")
	public Employee getEmployee(@PathVariable Long id) {
		Employee employee = employeeService.getEmployee(id);
		return employee;
	}

	@GetMapping
	public List<Employee> getAllEmployees() {
		List<Employee> allEmployees = employeeService.getAllEmployees();
		return allEmployees;

	}

	@PutMapping("/{id}")
	public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
		Employee updateEmployee = employeeService.updateEmployee(id, employee);
		return updateEmployee;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
		String message = employeeService.deleteEmployee(id);
		return ResponseEntity.ok(message);
	}

}
