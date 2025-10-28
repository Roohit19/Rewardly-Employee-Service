package com.rewardly.emp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rewardly.emp.entity.Employee;
import com.rewardly.emp.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public String createEmployee(Employee employee) {
		// Logic to save employee to the database

		Employee e = employeeRepository.save(employee);

		if (e != null) {
			return "Employee created successfully";
		} else {
			return "Failed to create employee";
		}

	}

}
