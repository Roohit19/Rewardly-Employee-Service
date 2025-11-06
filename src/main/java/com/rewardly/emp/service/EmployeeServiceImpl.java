package com.rewardly.emp.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rewardly.emp.employeedto.EmployeeRequest;
import com.rewardly.emp.employeedto.EmployeeResponse;
import com.rewardly.emp.entity.Employee;
import com.rewardly.emp.exception.EmployeeNotFoundException;
import com.rewardly.emp.mapper.EmployeeMapper;
import com.rewardly.emp.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {


	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper employeeMapper;
	
	@Transactional
	public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
		// Logic to save employee to the database
		Employee employee = employeeMapper.toEntity(employeeRequest);
		
		// Generate a human-friendly unique ID (timestamp + random digits)
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        int randomDigits = ThreadLocalRandom.current().nextInt(1000, 10000);
        String generatedId = String.format("rewardlyEmp-%s-%04d", timestamp, randomDigits);
        employee.setEmpId(generatedId);

		Employee savedEmployee = employeeRepository.save(employee);
		//return savedEmployee;
		return employeeMapper.toResponse(savedEmployee);


	}
	
	public EmployeeResponse getEmployee(String id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(()->{
							log.error("Employee not found: {}",id);
							return  new EmployeeNotFoundException("Employee not found from get method");
								}
							);
		
		return employeeMapper.toResponse(employee);
	}

	public List<EmployeeResponse> getAllEmployees() {
		List<Employee> allEmployees = employeeRepository.findAll();
		
		List<EmployeeResponse> responseList = employeeMapper.toResponseList(allEmployees);
		
		return responseList;
	}
@Transactional
	public Employee updateEmployee(String id, Employee employee) {
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

	public String deleteEmployee(String id) {
		Optional<Employee> byId = employeeRepository.findById(id);
		Employee employee = byId.get();
		employeeRepository.delete(employee);
		return "Employee Deleted successfully";

	}

}
