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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Slf4j
@Validated // added for validation check at the class level
@Tag(name = "Rewardly Employee Service", description = "API for employee CRUD operations")
public class EmployeeController {
//Manual Logging -alternative is @Slf4j
//	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	private final EmployeeService employeeService;

	@PostMapping
	@Operation(summary = "Create a employee record", description = "Create a new employee with provided details also validate the inputs")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Employee created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class), examples = @ExampleObject(name = "Success Response", value = """
					{
					    "success": true,
					    "statusCode": 201,
					    "message": "Employee created successfully",
					    "data": {
					        "empId": "rewardlyEmp-20251106-164405-8157",
					        "empName": "Rohit Sharma",
					        "email": "rohit@rewardly.in",
					        "department": "Engineering",
					        "designation": "Senior Developer"
					    },
					    "path": "/api/v1/employees",
					    "timestamp": "2025-11-13T10:30:00"
					}
					"""))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data - Validation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class), examples = {
					@ExampleObject(name = "Validation Error - Name and Email", summary = "Missing required fields", value = """
							{
							    "success": false,
							    "statusCode": 400,
							    "message": "Validation failed",
							    "errors": {
							        "empName": "Employee name is required",
							        "email": "Invalid email format"
							    },
							    "path": "/api/v1/employees",
							    "timestamp": "2025-11-13T10:30:00"
							}
							"""),
					@ExampleObject(name = "Validation Error - Experience and Rating", summary = "Invalid range values", value = """
							{
							    "success": false,
							    "statusCode": 400,
							    "message": "Validation failed",
							    "errors": {
							        "employeeExperience": "Experience must be between 0-50",
							        "performanceRating": "Should be in between 1-5"
							    },
							    "path": "/api/v1/employees",
							    "timestamp": "2025-11-13T10:30:00"
							}
							""") })),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Conflict - Employee already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class), examples = @ExampleObject(name = "Conflict Error", value = """
					{
					    "success": false,
					    "statusCode": 409,
					    "message": "Employee with name: Rahul Sharma already exists",
					    "path": "/api/v1/employees",
					    "timestamp": "2025-11-13T10:30:00"
					}
					"""))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class), examples = @ExampleObject(name = "Server Error", value = """
					{
					    "success": false,
					    "statusCode": 500,
					    "message": "An unexpected error occurred while creating employee",
					    "path": "/api/v1/employees",
					    "timestamp": "2025-11-13T10:30:00"
					}
					"""))) })
	public ResponseEntity<ApiResponse<EmployeeResponse>> createEmployee(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Employee data to create", required = true, content = @Content(schema = @Schema(implementation = EmployeeRequest.class), examples = @ExampleObject(value = """
					{
						"empName": "Sunil",
						 "empDesignation": "HR Executive",
						 "empSalary": 55000.00,
						 "empExperienceYears": 3.5,
						 "empPerformanceRating": 5
						 }
					"""))) @Valid @RequestBody EmployeeRequest employeeRequest,
			HttpServletRequest request) {
		log.info("Api Request: Creatin new employee with name: {}", employeeRequest.getEmpName());
		EmployeeResponse employeeSaved = employeeService.createEmployee(employeeRequest);
		ApiResponse<EmployeeResponse> apiResponse = ApiResponse.<EmployeeResponse>builder().success(true)
				.statusCode(HttpStatus.CREATED.value()).message("Employee created successfully").data(employeeSaved)
				.path(request.getRequestURI()).build();
		log.info("Api Response: Successfully created employee with ID: {}", employeeSaved.getEmpId());

		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);

		// return ResponseEntity.ok();
	}

	@GetMapping("/hello")
	public String hello() {
		return "Hello from Employee Service";
	}

	@GetMapping("/{id}")
	@Operation(	summary = "Featch employee by ID",
				description = "Fetch complete employee record by employee id"				
			)
	@ApiResponses(value = {
		    @io.swagger.v3.oas.annotations.responses.ApiResponse(
		        responseCode = "200",
		        description = "Employee retrieved successfully",
		        content = @Content(
		            mediaType = "application/json",
		            schema = @Schema(implementation = ApiResponse.class),
		            examples = @ExampleObject(
		                name = "Success Response",
		                value = """
		                {
		                    "success": true,
		                    "statusCode": 200,
		                    "message": "Employee retrieved successfully",
		                    "data": {
		                        "empId": "rewardlyEmp-20251106-164405-8157",
		                        "empName": "Rohit Sharma",
		                        "empDepartment": "Engineering",
		                        "empDesignation": "Senior Developer",
		                        "empPerformanceRating": "5"
		                    },
		                    "path": "/api/v1/employees/rewardlyEmp-20251106-164405-8157",
		                    "timestamp": "2025-11-13T10:30:00"
		                }
		                """
		            )
		        )
		    ),
			/*
			 * @io.swagger.v3.oas.annotations.responses.ApiResponse( responseCode = "400",
			 * description = "Invalid employee ID", content = @Content( mediaType =
			 * "application/json", schema = @Schema(implementation = ApiResponse.class),
			 * examples = @ExampleObject( name = "Invalid ID", value = """ { "success":
			 * false, "statusCode": 400, "message": "Invalid employee ID format", "path":
			 * "/api/v1/employees/rewardly-20251106-164405-8157", "timestamp":
			 * "2025-11-13T10:30:00" } """ ) ) ),
			 */
		    @io.swagger.v3.oas.annotations.responses.ApiResponse(
		        responseCode = "404",
		        description = "Employee not found",
		        content = @Content(
		            mediaType = "application/json",
		            schema = @Schema(implementation = ApiResponse.class),
		            examples = @ExampleObject(
		                name = "Not Found Error",
		                value = """
		                {
		                    "success": false,
		                    "statusCode": 404,
		                    "message": "Employee with ID: rewardlyEmp-20251106-164405-8157 not found",
		                    "path": "/api/v1/employees/rewardlyEmp-20251106-164405-8157",
		                    "timestamp": "2025-11-13T10:30:00"
		                }
		                """
		            )
		        )
		    ),
		    @io.swagger.v3.oas.annotations.responses.ApiResponse(
		        responseCode = "500",
		        description = "Internal server error",
		        content = @Content(
		            mediaType = "application/json",
		            schema = @Schema(implementation = ApiResponse.class),
		            examples = @ExampleObject(
		                name = "Server Error",
		                value = """
		                {
		                    "success": false,
		                    "statusCode": 500,
		                    "message": "An unexpected error occurred while retrieving employee",
		                    "path": "/api/v1/employees/rewardlyEmp-20251106-164405-8157",
		                    "timestamp": "2025-11-13T10:30:00"
		                }
		                """
		            )
		        )
		    )
		})
	public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployee(@PathVariable String id,
			HttpServletRequest request) {
		EmployeeResponse employeeResponse = employeeService.getEmployee(id);
		ApiResponse<EmployeeResponse> apiResponse = ApiResponse.<EmployeeResponse>builder().success(true)
				.statusCode(HttpStatus.OK.value()).message("Employee retrieved successfully").data(employeeResponse)
				.path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getAllEmployees(HttpServletRequest request) {
		List<EmployeeResponse> allEmployees = employeeService.getAllEmployees();
		ApiResponse<List<EmployeeResponse>> apiResponse = ApiResponse.<List<EmployeeResponse>>builder().success(true)
				.statusCode(HttpStatus.OK.value()).message("All employees retrieved successfully").data(allEmployees)
				.path(request.getRequestURI()).build();
		return ResponseEntity.ok(apiResponse);

	}

	@PutMapping("/{id}")
	public EmployeeResponse updateEmployee(@PathVariable String id,
			@Valid @RequestBody EmployeeRequest employeeRequest) {
		EmployeeResponse updateEmployee = employeeService.updateEmployee(id, employeeRequest);
		return updateEmployee;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable String id, HttpServletRequest request) {
		log.info("Deleting employee with Id: {}", id);
		log.info("Deleting the employee Path: {}", request.getRequestURI());
		employeeService.deleteEmployee(id);
		log.info("Employee successfully deleted with Id: {}", id);
		return ResponseEntity.noContent().build();
	}

}
