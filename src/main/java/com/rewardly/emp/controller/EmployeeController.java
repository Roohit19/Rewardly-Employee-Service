package com.rewardly.emp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rewardly.emp.entity.Employee;
import com.rewardly.emp.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/create")
	public ResponseEntity<String> createEmployee(@RequestBody Employee employee) {

		return ResponseEntity.ok(employeeService.createEmployee(employee));
	}
	
	
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello from Employee Service";
	}

}
