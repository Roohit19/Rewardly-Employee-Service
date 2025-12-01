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



import com.rewardly.emp.employeedto.EmployeeApiResponse;
import com.rewardly.emp.employeedto.EmployeeRequest;
import com.rewardly.emp.employeedto.EmployeeResponse;
import com.rewardly.emp.exception.ErrorResponse;
import com.rewardly.emp.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
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
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Employee created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeApiResponse.class), examples = @ExampleObject(name = "Success Response", value = """
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
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data - Validation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation =EmployeeApiResponse.class), examples = {
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
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Conflict - Employee already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeApiResponse.class), examples = @ExampleObject(name = "Conflict Error", value = """
					{
					    "success": false,
					    "statusCode": 409,
					    "message": "Employee with name: Rahul Sharma already exists",
					    "path": "/api/v1/employees",
					    "timestamp": "2025-11-13T10:30:00"
					}
					"""))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeApiResponse.class), examples = @ExampleObject(name = "Server Error", value = """
					{
					    "success": false,
					    "statusCode": 500,
					    "message": "An unexpected error occurred while creating employee",
					    "path": "/api/v1/employees",
					    "timestamp": "2025-11-13T10:30:00"
					}
					"""))) })
	public ResponseEntity<EmployeeApiResponse<EmployeeResponse>> createEmployee(
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
		log.info("Api Request: Creating new employee with name: {}", employeeRequest.getEmpName());
		EmployeeResponse employeeSaved = employeeService.createEmployee(employeeRequest);
		EmployeeApiResponse<EmployeeResponse> apiResponse = EmployeeApiResponse.<EmployeeResponse>builder().success(true)
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
		            schema = @Schema(implementation = EmployeeApiResponse.class),
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
			
//			@io.swagger.v3.oas.annotations.responses.ApiResponse( responseCode = "400",
//			 description = "Invalid employee ID", content = @Content( mediaType =
//			 "application/json", schema = @Schema(implementation = ApiResponse.class),
//			 examples = @ExampleObject( name = "Invalid ID", value = """ { "success":
//			 false, "statusCode": 400, "message": "Invalid employee ID format", "path":
//			 "/api/v1/employees/rewardly-20251106-164405-8157", "timestamp":
//			 "2025-11-13T10:30:00" } """ ) ) ),
			 
		    @io.swagger.v3.oas.annotations.responses.ApiResponse(
		        responseCode = "404",
		        description = "Employee not found",
		        content = @Content(
		            mediaType = "application/json",
		            schema = @Schema(implementation = EmployeeApiResponse.class),
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
		            schema = @Schema(implementation = EmployeeApiResponse.class),
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
	public ResponseEntity<EmployeeApiResponse<EmployeeResponse>> getEmployee(@PathVariable 
			String id,
			HttpServletRequest request) {
		EmployeeResponse employeeResponse = employeeService.getEmployee(id);
		EmployeeApiResponse<EmployeeResponse> apiResponse = EmployeeApiResponse.<EmployeeResponse>builder().success(true)
				.statusCode(HttpStatus.OK.value()).message("Employee retrieved successfully").data(employeeResponse)
				.path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
	}

	@GetMapping
	public ResponseEntity<EmployeeApiResponse<List<EmployeeResponse>>> getAllEmployees(HttpServletRequest request) {
		List<EmployeeResponse> allEmployees = employeeService.getAllEmployees();
		EmployeeApiResponse<List<EmployeeResponse>> apiResponse = EmployeeApiResponse.<List<EmployeeResponse>>builder().success(true)
				.statusCode(HttpStatus.OK.value()).message("All employees retrieved successfully").data(allEmployees)
				.path(request.getRequestURI()).build();
		return ResponseEntity.ok(apiResponse);

	}
	
@Operation(summary = "Update an existing employee", description = "Updates an employee's information by their unique ID. "
		+ "All fields in the request body will update the corresponding employee record."+" "
				+ "Returns wrapped response with success status, data , message, path, status code and timestamp.")
@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
				description = "Employee successfully updated",
				content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = EmployeeApiResponse.class),
				examples = @ExampleObject(name = "Successful update",
				value="""
						{
							"success": true,
							"data": {
										"empId": "rewardlyEmp-20251106-164405-8157",
										"empName": "Riya Singh",
										"empDesignation": "Senior Manager",
										"empSalary": 95000,
										"empExperienceYears": 7,
										"empPerformanceRating": 4
									},
							"message": "Employee updated successfully",
							"path": "/api/v1/employees/rewardlyEmp-20251106-164405-8157",
							"statusCode": 200,
							"timeStamp": "2025-11-22T10:30:00"
				}"""))),
		@ApiResponse(
				responseCode = "400",
				description = "Invalid request - Validation errors in request body or invalid ID format",
				content = @Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ErrorResponse.class),
						examples = {
								@ExampleObject(
								name = "Invalid Employee ID Format",
								value = """
										{
											"success": false,
											"status": 400,
											"errorCode": "VALIDATION_ERROR",
											"errorMessage": "Invalid employee ID format",
											"details": "Employee ID must follow format: rewardlyEmp-YYYYMMDD-HHMMSS-####",
											"path": "/api/v1/employees/invalid-id-123",
											"timeStamp": "2025-11-22T10:30:00"
										}
										"""),
								@ExampleObject(
								name = "Name Validation Error",
								value = """
										{
											"success": false,
											"status": 400,
											"errorCode": "VALIDATION_ERROR",
											"errorMessage": "Validation failed for employee update",
											"details": "One or more fields contain invalid data",
											"path": "/api/v1/employees/rewardlyEmp-20251106-164405-8157",
											"errors": {
														"empName": "Name must contain only letters and spaces",
														"empSalary": "Salary must be positive"
													},
											"timeStamp": "2025-11-22T10:30:00"
										}
										"""),
								@ExampleObject(
										name = "Empty Name Error",
										value = """
												{
													"success": false,
													"status": 400,
													"errorCode": "VALIDATION_ERROR",
													"errorMessage": "Validation failed for employee update",
													"path": "/api/v1/employees/rewardlyEmp-20251106-164405-8157",
													"errors": { 
																"empName": "Employee name is required"
															},
													"timeStamp": "2025-11-22T10:30:00"
												}
												"""),
								@ExampleObject(
										name = "Name Length Error",
										value = """
												{
													"success": false,
													"status": 400,
													"errorCode": "VALIDATION_ERROR",
													"errorMessage": "Validation failed for employee update",
													"path": "/api/v1/employees/rewardlyEmp-20251106-164405-8157",
													"errors":{
																"empName": "Name must be between 2 and 100 characters"
															},
													"timeStamp": "2025-11-22T10:30:00"
												}
												"""),
								@ExampleObject(
										name = "Designation Validation Error",
										value = """
												{
													"success": false,
													"status": 400,
													"errorCode": "VALIDATION_ERROR",
													"errorMessage": "Validation failed for employee update",
													"path": "/api/v1/employees/rewardlyEmp-20251106-1664405-8157",
													"errors": {
																"empDesignation": "Designation is required",
																"empDesignation": "Name must contain only letters and spaces"
															},
													"timeStamp": "2025-11-22T10:30:00"
												}
												"""),
								@ExampleObject(
										name = "Salary Validation Error",
										value = """
												{
													"success": false,
													"status": 400,
													"errorCode": "VALIDATION_ERROR",
													"errorMessage": "Validation failed for employee update",
													"path": "/api/v1/employees/rewardlyEmp-20251106-1664405-8157",
													"errors": {
																"empSalary": "Salary must be positive"
															  },
													"timeStamp": "2025-11-22T10:30:00"
												}
												"""),
								@ExampleObject(
										name = "Experience Years Validation Error",
										value = """
												{
													"success": false,
													"status": 400,
													"errorCode": "VALIDATION_ERROR",
													"errorMessage": "Validation failed for employee update",
													"path": "/api/v1/employees/rewardlyEmp-20251106-1664405-8157",
													"errors": {
																"empExperienceYears": "Years value must be positive and required"
															},
													"timeStamp": "2025-11-22T10:30:00"
												}
												"""),
								@ExampleObject(
										name = "Performance Rating Range Error",
				                        value = """
			                            {
			                                "success": false,
			                                "status": 400,
			                                "errorCode": "VALIDATION_ERROR",
			                                "errorMessage": "Validation failed for employee update",
			                                "path": "/api/v1/employees/rewardlyEmp-20251106-1664405-8157",
			                                "errors": {
			                                    "empPerformanceRating": "Performance rating must be up to 5"
			                                },
			                                "timestamp": "2025-11-22T10:30:00"
			                            }
			                            """
			                    ),
			                    @ExampleObject(
			                        name = "Multiple Validation Errors",
			                        value = """
			                            {
			                                "success": false,
			                                "status": 400,
			                                "errorCode": "VALIDATION_ERROR",
			                                "errorMessage": "Multiple validation errors occurred",
			                                "details": "Please correct the highlighted fields",
			                                "path": "/api/v1/employees/rewardlyEmp-20251106-1664405-8157",
			                                "errors": {
			                                    "empName": "Employee name is required",
			                                    "empDesignation": "Designation is required",
			                                    "empSalary": "salary must be positive",
			                                    "empPerformanceRating": "Performance rating must be at least 1"
			                                },
			                                "timestamp": "2025-11-22T10:30:00"
			                            }
			                            """
			                    )
			                }
			            )
			        ),
		@ApiResponse(
				responseCode = "404",
				description = "Employee Not Found - No employee exist with the provided ID",
				content = @Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ErrorResponse.class),
						examples = @ExampleObject(
								name = "Employee not found",
								value = """
										{
											"success": false,
											"status": 404,
											"errorCode": "EMPLOYEE_NOT_FOUND",
											"details": "Employee with ID 'rewardlyEmp-20251106-164405-8157' does not exist in the system",
											"path": "/api/v1/employees/rewardlyEmp-20251106-164405-8157",
											"timeStamp": "2025-11-22T10:30:00"
										}
										"""))
				),
		@ApiResponse(
				responseCode = "409",
				description = "Conflict - Business rule violation or duplicate data",
				content = @Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ErrorResponse.class),
						examples = {
								@ExampleObject(
								name = "Duplicate Employee Name",
								value = """
										{
											"success": false,
											"status": 409,
											"errorCode": "DUPLICATE_EMPLOYEE",
											"errorMessage": "Duplicate employee record",
											"details": "An employee with name "Riya Singh" already exists",
											"path": "/api/v1/employees/rewardlyEmp-20251106-164405-8157",
											"timeStamp": "2025-11-22T10:30:00"
										}
										"""),
						@ExampleObject(
								name = "Business Rule Violation",
								value = """
										{
											"success": false,
											"status": 409,
											"errorCode": "BUSINESS_RULE_VIOLATION",
											"errorMessage": "Update violates business rules",
											"details": "Cannot update employee designation while performance rating is below 3",
											"path": "/api/v1/employees/rewardlyEmp-20251106-164405-8157",
											"timeStamp": "2025-11-22T10:30:00"
										}
																				""")})),
		@ApiResponse(
				responseCode = "500",
				description = "Internal server error - Unexpected error occured while processing the request",
				content = @Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ErrorResponse.class),
						examples = @ExampleObject(
								name = "Server Error",
								value = """
										{
											"success": false,
											"status": 500,
											"errorCode": "INTERNAL_SERVER_ERROR",
											"errorMessage": "An unexpected error occured",
											"details": "Failed to update employee due to database connection error",
											"path": "/api/v1/employees/rewardlyEmp-20251106-164405-8157",
											"timeStamp": "2025-11-22T10:30:00"
										}
																				""")))
		
							
						
						})
		
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeApiResponse<EmployeeResponse>> updateEmployee(
			@Parameter(
					description = "Unique identifier of the employee to update. Format: rewardlyEmp-YYYYMMDD-HHMMSS-#### (e.g., rewardlyEmp-20251106-164405-8157)",
					required = true,
					example = "rewardlyEmp-20251106-164405-8157",
					schema = @Schema(type = "string", pattern = "^rewardlyEmp-\\\\d{8}-\\\\d{6}-\\\\d{4}$"))
			
			@PathVariable 
			@Pattern(regexp = "^rewardlyEmp-\\d{8}-\\d{6}-\\d{4}$", message = "Employee ID must follow format: rewardlyEmp-YYYYMMDD-HHMMSS-####")
			String id,
			@Parameter(
					description = "Employee data to update with validation rules applied",
					required = true,
					content = @Content(
							schema = @Schema(implementation = EmployeeRequest.class),
							examples = {
									@ExampleObject(
									name = "Valid Update Request",
									value = """
											{
												 "empName": "Riya Singh",
											     "empDesignation": "Senior Manager",
											     "empSalary": 95000,
											     "empExperienceYears": 7,
											     "empPerformanceRating": 4
											}
											"""),
									@ExampleObject(  name = "Minimal Update Request",
					                        value = """
				                            {
				                                "empName": "Amit Kumar",
				                                "empDesignation": "Developer",
				                                "empSalary": 60000,
				                                "empExperienceYears": 2,
				                                "empPerformanceRating": 3
				                            }
				                            """
				                    ),
				                    @ExampleObject(
				                        name = "Maximum Values Request",
				                        value = """
				                            {
				                                "empName": "Priya Sharma",
				                                "empDesignation": "Chief Technology Officer",
				                                "empSalary": 250000,
				                                "empExperienceYears": 5,
				                                "empPerformanceRating": 5
				                            }
				                        		"""
				                        )
							}))
			@Valid @RequestBody EmployeeRequest employeeRequest,
			HttpServletRequest request) {
		EmployeeResponse updatedEmployee = employeeService.updateEmployee(id, employeeRequest);
		EmployeeApiResponse<EmployeeResponse> employeeResponse = EmployeeApiResponse.<EmployeeResponse>builder()
		.success(true)
		.statusCode(HttpStatus.OK.value())
		.data(updatedEmployee)
		.message("Employee updated successfully")
		.path(request.getRequestURI())
		.build();
		return ResponseEntity.ok(employeeResponse);
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
