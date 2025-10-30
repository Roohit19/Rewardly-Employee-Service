package com.rewardly.emp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rewardly.emp.entity.Employee;
import com.rewardly.emp.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {


	private final EmployeeRepository employeeRepository;

	public String createEmployee(Employee employee) {
		// Logic to save employee to the database

		Employee e = employeeRepository.save(employee);

		if (e != null) {
			return "Employee created successfully";
		} else {
			return "Failed to create employee";
		}

	}

	public Employee getEmployee(int id) {
		Optional<Employee> emp = employeeRepository.findById(id);
		return emp.get();
	}

	public List<Employee> getAllEmployees() {
		List<Employee> allEmployees = employeeRepository.findAll();
		return allEmployees;
	}

	public Employee updateEmployee(int id, Employee employee) {
		Optional<Employee> byId = employeeRepository.findById(id);
		Employee employee2 = byId.get();
		employee2.setEmpName(employee.getEmpName());
		employee2.setEmpDesignation(employee.getEmpDesignation());
		employee2.setEmpSalary(employee.getEmpSalary());
		employee2.setEmpExperience(employee.getEmpExperience());
		employee2.setEmpPerformanceRating(employee.getEmpPerformanceRating());
		Employee savedEmployee = employeeRepository.save(employee2);
		return savedEmployee;
	}

	public String deleteEmployee(int id) {
		Optional<Employee> byId = employeeRepository.findById(id);
		Employee employee = byId.get();
		employeeRepository.delete(employee);
		return "Employee Deleted successfully";

	}

}
