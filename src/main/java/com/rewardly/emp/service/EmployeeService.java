package com.rewardly.emp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rewardly.emp.employeedto.EmployeeRequest;
import com.rewardly.emp.employeedto.EmployeeResponse;
import com.rewardly.emp.entity.Employee;
import com.rewardly.emp.mapper.EmployeeMapper;
import com.rewardly.emp.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {


	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper employeeMapper;

	public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
		// Logic to save employee to the database
		Employee employee = employeeMapper.toEntity(employeeRequest);
		Employee savedEmployee = employeeRepository.save(employee);
		//return savedEmployee;
		return employeeMapper.toResponse(savedEmployee);


	}

	public EmployeeResponse getEmployee(Long id) {
		Optional<Employee> employeeOptional = employeeRepository.findById(id);
		Employee employee = employeeOptional.get();
		
		return employeeMapper.toResponse(employee);
	}

	public List<EmployeeResponse> getAllEmployees() {
		List<Employee> allEmployees = employeeRepository.findAll();
		
		List<EmployeeResponse> responseList = employeeMapper.toResponseList(allEmployees);
		
		return responseList;
	}

	public Employee updateEmployee(Long id, Employee employee) {
		Optional<Employee> byId = employeeRepository.findById(id);
		Employee employee2 = byId.get();
//		employee2.setEmpName(employee.getEmpName());
//		employee2.setEmpDesignation(employee.getEmpDesignation());
//		employee2.setEmpSalary(employee.getEmpSalary());
//		employee2.setEmpExperienceYears(employee.getEmpExperienceYears());
//		employee2.setEmpPerformanceRating(employee.getEmpPerformanceRating());
//		Employee savedEmployee = employeeRepository.save(employee2);
		
		Employee savedEmployees=employee2.toBuilder()
        .empName(employee.getEmpName())
//        .empDesignation(employee.getEmpDesignation())
//        .empSalary(employee.getEmpSalary())
//        .empExperienceYears(employee.getEmpExperienceYears())
//        .empPerformanceRating(employee.getEmpPerformanceRating())
        .build();
		
		return employeeRepository.save(savedEmployees);
	}

	public String deleteEmployee(Long id) {
		Optional<Employee> byId = employeeRepository.findById(id);
		Employee employee = byId.get();
		employeeRepository.delete(employee);
		return "Employee Deleted successfully";

	}

}
