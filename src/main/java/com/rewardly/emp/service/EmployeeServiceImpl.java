package com.rewardly.emp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
//import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rewardly.emp.employeedto.EmployeeRequest;
import com.rewardly.emp.employeedto.EmployeeResponse;
import com.rewardly.emp.entity.Employee;
import com.rewardly.emp.exception.EmployeeNotFoundException;
import com.rewardly.emp.exception.InvalidEmployeeDataException;
import com.rewardly.emp.mapper.EmployeeMapper;
import com.rewardly.emp.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service Layer for employee business logic implementation Handles all employee
 * related operations
 * 
 * @author Rewardly Team
 * @version 1.0
 * @since 2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper employeeMapper;

	/**
	 * Create a new employee in the system
	 * 
	 * @param employeeRequest the employee data to be saved in database
	 * @return EmployeeResponse containing saved employee details
	 * @throws InvalidEmployeeDataException for if experience should be up to 50
	 *                                      also for performance rating must be
	 *                                      between 1 to 5
	 * 
	 */
	@Transactional
	public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
		// Logic to save employee to the database
		log.debug("Creating employee with name: {}", employeeRequest.getEmpName());
		Employee employee = employeeMapper.toEntity(employeeRequest);

		if (employeeRequest.getEmpExperienceYears().compareTo(BigDecimal.valueOf(50)) > 0) {
			log.error("Invalid experiece: {}", employeeRequest.getEmpExperienceYears());
			throw new InvalidEmployeeDataException(
					String.format("experience must be less than 50.Provided experience: %.1f years",
							employeeRequest.getEmpExperienceYears()));
		}
		if (employeeRequest.getEmpPerformanceRating() < 1 || employeeRequest.getEmpPerformanceRating() > 5) {
			log.error("Invalid performance rating: {}", employeeRequest.getEmpPerformanceRating());
			throw new InvalidEmployeeDataException(
					String.format("Performance rating must be between 1 and 5.Provided performance rating: %d ",
							employeeRequest.getEmpPerformanceRating(), employeeRequest.getEmpName()));
		}

		// Generate a human-friendly unique ID (timestamp + random digits)
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
		int randomDigits = ThreadLocalRandom.current().nextInt(1000, 10000);
		String generatedId = String.format("rewardlyEmp-%s-%04d", timestamp, randomDigits);
		employee.setEmpId(generatedId);
		log.debug("Saving employee with name: {} and Id:{}", employee.getEmpName(), employee.getEmpId());
		Employee savedEmployee = employeeRepository.save(employee);
		// return savedEmployee;
		log.info("Employee saved Id:{}", employee.getEmpId());
		return employeeMapper.toResponse(savedEmployee);

	}

	/**
	 * Retrieves employee by ID
	 * 
	 * @param id -the employee id
	 * @return EmployeeResponse -Containing the employee details
	 * @throws EmployeeNotFoundException -if employee not found
	 */
	public EmployeeResponse getEmployee(String id) {
		log.debug("Fetching employee with id: {}", id);

		Employee employee = employeeRepository.findById(id).orElseThrow(() -> {
			log.error("Employee not found: {}", id);
			return new EmployeeNotFoundException("Employee not found from get method");
		});
		log.info("Successfully retrieve employee record with name: {} and Id: {}", employee.getEmpName(),
				employee.getEmpId());
		return employeeMapper.toResponse(employee);
	}

	/**
	 * Retrieve all employees in the system
	 * 
	 * @return List of EmployeeResponse
	 */
	public List<EmployeeResponse> getAllEmployees() {
		log.debug("Fetchin all employees");
		List<Employee> allEmployees = employeeRepository.findAll();
		log.info("Retrieved employees from database: {}", getAllEmployees().size());

		List<EmployeeResponse> responseList = employeeMapper.toResponseList(allEmployees);

		return responseList;
	}

	/**
	 * Updates an existing employee's record/information
	 * 
	 * @param id              -The employee id to update
	 * @param EmployeeRequest - The employee data to be updated
	 * @return EmployeeResponse - Containing the updated employee details
	 * @throws EmployeeNotFoundException -If employee with specific Id is not present in DB
	 * 
	 */
	@Transactional
	public EmployeeResponse updateEmployee(String id, EmployeeRequest employeeRequest) {
		log.debug("Updating employee with Id: {}", id);
		Employee existingEmployee = employeeRepository.findById(id).orElseThrow(() -> {
			log.error("Employee not found with Id: {}", id);
			return new EmployeeNotFoundException(String.format("Employee not found with Id: %s", id));
		});

		employeeMapper.updateEntityFromRequest(employeeRequest, existingEmployee);
		Employee updatedEmployee = employeeRepository.save(existingEmployee);
		log.info("Successfully updated employee with Id: {}", id);
		return employeeMapper.toResponse(updatedEmployee);

//		Employee employee2 = byId.get();
//		employee2.setEmpName(employee.getEmpName());
//		employee2.setEmpDesignation(employee.getEmpDesignation());
//		employee2.setEmpSalary(employee.getEmpSalary());
//		employee2.setEmpExperienceYears(employee.getEmpExperienceYears());
//		employee2.setEmpPerformanceRating(employee.getEmpPerformanceRating());
//		Employee savedEmployee = employeeRepository.save(employee2);

//		Employee savedEmployees=employee2.toBuilder()
//        .empName(employee.getEmpName())
//        .empDesignation(employee.getEmpDesignation())
//        .empSalary(employee.getEmpSalary())
//        .empExperienceYears(employee.getEmpExperienceYears())
//        .empPerformanceRating(employee.getEmpPerformanceRating())
//        .build();

//		return employeeRepository.save(savedEmployees);
	}

	/**
	 * Deletes an employee
	 * @param id -the employee id to delete
	 * @throws EmployeeNotFoundException -if employee not Found
	 */
	public void deleteEmployee(String id) {
		Employee existingEmployee = employeeRepository.findById(id).orElseThrow(()->{
		log.error("Employee not found with Id: {} and name: {}",id);
		return new EmployeeNotFoundException(
				String.format("Employee not found with Id: %s and name: %s",id));
				});
		log.info("Employee found with name: {}", existingEmployee.getEmpName());
	
//		EmployeeNOtFoundException -> spring -> ArgumentMismatchException implementation class of handler
//		-> implementd custom exception -> Global exception ->RestControlerAdvice
//		-> handlerEmployeeNOtFoundException -> ErrorResponse -> build (message) 
//		-> ResponseEntity<ErrorResponse>
		
employeeRepository.delete(existingEmployee);

//if(!employeeRepository.existsById(id)) {
//	log.error("Employee not found with id: {}",id);
//	throw new EmployeeNotFoundException(
//			String.format("Employee not found with Id: %s",id));
//}
//employeeRepository.deleteById(id);
		

	}

}
