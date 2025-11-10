package com.rewardly.emp.service;

import java.util.List;

import com.rewardly.emp.employeedto.EmployeeRequest;
import com.rewardly.emp.employeedto.EmployeeResponse;

public interface EmployeeService {
/**
 * Creating employee with employeeRequest data
 * @param employeeRequest
 * @return
 */
	public EmployeeResponse createEmployee(EmployeeRequest employeeRequest);
/**
 * Fetching employeeResponse data with id
 * @param id
 * @return EmployeeResponse containing employee data
 */
	public EmployeeResponse getEmployee(String id);
/**
 * Fetching all employees data available in system
 * @return EmployeeResponse list containing employee data
 */
	public List<EmployeeResponse> getAllEmployees();
/**
 * Updating existing employee with provided data to update
 * @param id
 * @param employeeRequest
 * @return EmployeeResponse updated employee data
 */
	public EmployeeResponse updateEmployee(String id, EmployeeRequest employeeRequest);
/**
 * Deleting employee 
 * @param id employee id to be deleted
 */
	public void deleteEmployee(String id);

}
