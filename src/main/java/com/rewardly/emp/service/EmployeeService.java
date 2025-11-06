package com.rewardly.emp.service;

import java.util.List;

import com.rewardly.emp.employeedto.EmployeeRequest;
import com.rewardly.emp.employeedto.EmployeeResponse;
import com.rewardly.emp.entity.Employee;

public interface EmployeeService {

	public EmployeeResponse createEmployee(EmployeeRequest employeeRequest);

	public EmployeeResponse getEmployee(String id);

	public List<EmployeeResponse> getAllEmployees();

	public Employee updateEmployee(String id, Employee employee);

	public String deleteEmployee(String id);

}
