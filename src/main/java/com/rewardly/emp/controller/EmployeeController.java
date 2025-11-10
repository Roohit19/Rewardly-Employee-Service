package com.rewardly.emp.controller;

import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rewardly.emp.employeedto.ApiResponse;
import com.rewardly.emp.employeedto.EmployeeRequest;
import com.rewardly.emp.employeedto.EmployeeResponse;
import com.rewardly.emp.service.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Slf4j
@Validated  //added for validation check at the class level
public class EmployeeController {
//Manual Logging -alternative is @Slf4j
//	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);


	private final EmployeeService employeeService;

	@PostMapping
	public ResponseEntity<ApiResponse<EmployeeResponse>> createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest, 
		HttpServletRequest request) {
		log.info("Api Request: Creatin new employee with name: {}",employeeRequest.getEmpName());
		EmployeeResponse employeeSaved = employeeService.createEmployee(employeeRequest);
		ApiResponse<EmployeeResponse> apiResponse = ApiResponse.<EmployeeResponse>builder()
		.success(true)
		.statusCode(HttpStatus.CREATED.value())
		.message("Employee created successfully")
		.data(employeeSaved)
		.path(request.getRequestURI())
		.build();
		log.info("Api Response: Successfully created employee with ID: {}",employeeSaved.getEmpId() );
		
		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);

		//return ResponseEntity.ok();
	}

	@GetMapping("/hello")
	public String hello() {
		return "Hello from Employee Service";
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployee(@PathVariable String id,  HttpServletRequest request) {
		EmployeeResponse employeeResponse = employeeService.getEmployee(id);
		ApiResponse<EmployeeResponse> apiResponse = ApiResponse.<EmployeeResponse>builder()
				   .success(true)
				   .statusCode(HttpStatus.OK.value())
				   .message("Employee retrieved successfully")
				   .data(employeeResponse)
				   .path(request.getRequestURI())
				   .build();
		
		
		return  ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
	}

	@GetMapping
	public ApiResponse<List<EmployeeResponse>> getAllEmployees(HttpServletRequest request) {
		 List<EmployeeResponse> allEmployees = employeeService.getAllEmployees();
		 ApiResponse<List<EmployeeResponse>> apiResponse = ApiResponse.<List<EmployeeResponse>>builder()
		 		    .success(true)
		 		    .statusCode(HttpStatus.OK.value())
		 		    .message("All employees retrieved successfully")
		 		    .data(allEmployees)
		 		    .path(request.getRequestURI())
		 		    .build(); 
		return apiResponse;

	}

	@PutMapping("/{id}")
	public EmployeeResponse updateEmployee(@PathVariable String id, @Valid @RequestBody EmployeeRequest employeeRequest) {
		EmployeeResponse updateEmployee = employeeService.updateEmployee(id, employeeRequest);
		return updateEmployee;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable String id,HttpServletRequest request) {
		log.info("Deleting employee with Id: {}",id);
		log.info("Deleting the employee Path: {}",request.getRequestURI());
		employeeService.deleteEmployee(id);
		log.info("Employee successfully deleted with Id: {}",id);
		return ResponseEntity.noContent().build();
	}

}
