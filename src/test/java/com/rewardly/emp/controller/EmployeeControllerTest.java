package com.rewardly.emp.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewardly.emp.employeedto.EmployeeRequest;
import com.rewardly.emp.employeedto.EmployeeResponse;
import com.rewardly.emp.exception.EmployeeNotFoundException;
import com.rewardly.emp.service.EmployeeService;


// This is a Slice test (Neither pure unit test nor 
@WebMvcTest(EmployeeController.class) // Set up test environment that include only web layer
class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc; // test bot that can mock HTTP requests to the controller

	@MockBean // Creates a mock version of the service layer
	private EmployeeService employeeService;

	// External data transformation -> Json -> Java
	@Autowired
	private ObjectMapper objectMapper;

	private String validEmpId;
	private EmployeeResponse employeeResponse;
	private EmployeeRequest employeeRequest;

	@BeforeEach
	void setUp() {

		// Creating a mock employee object that represents what the service would return

		validEmpId = "rewardlyEmp-20251118-190420-9480";
		employeeResponse = EmployeeResponse.builder().empId(validEmpId).empName("Wasim Shaikh")
				.empDesignation("Java Developer").empSalary(new BigDecimal("100000.00"))
				.empExperienceYears(new BigDecimal("3.0")).empPerformanceRating(5).build();

		employeeRequest = EmployeeRequest.builder().empName("Wasim Shaikh").empDesignation("Java Developer")
				.empSalary(new BigDecimal("100000.00")).empExperienceYears(new BigDecimal("3.0"))
				.empPerformanceRating(5).build();
	}

	// CreateEmployee
	@Test
	void testCreateEmployee() throws Exception {
		//Service layer mock
		when(employeeService.createEmployee(employeeRequest)).thenReturn(employeeResponse);
		
		//Mock web call
		mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeRequest))).andDo(print())
				.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.statusCode").value(201))
				.andExpect(jsonPath("$.message").value("Employee created successfully"))
				.andExpect(jsonPath("$.timeStamp").exists()).andExpect(jsonPath("$.data.empId").value(validEmpId)) // Check
				.andExpect(jsonPath("$.data.empName").value(employeeResponse.getEmpName())) // Check name
				.andExpect(jsonPath("$.data.empDesignation").value(employeeResponse.getEmpDesignation()))
				.andExpect(jsonPath("$.data.empExperienceYears").value(employeeResponse.getEmpExperienceYears()))
				.andExpect(jsonPath("$.data.empPerformanceRating").value(employeeResponse.getEmpPerformanceRating()))
				.andExpect(jsonPath("$.data.empSalary").value(employeeResponse.getEmpSalary().doubleValue()))// 100000.00
				.andExpect(jsonPath("$.path").value("/api/v1/employees"));

		//verify number of calls
		verify(employeeService, times(1)).createEmployee(employeeRequest);
	}

	// While creating invalid name
	@Test
	void shouldReturn400WhenNameIsMissing() throws Exception {

		employeeRequest = EmployeeRequest.builder().empDesignation("Java Developer")
				.empSalary(new BigDecimal("100000.00")).empExperienceYears(new BigDecimal("3.0"))
				.empPerformanceRating(5).build();

		mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeRequest))).andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false)).andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.errorMessage").value("Validation failed"))
				.andExpect(jsonPath("$.path").value("/api/v1/employees"))
				.andExpect(jsonPath("$.errors.empName").value("Employee name is required"))
				.andExpect(jsonPath("$.timestamp").exists());
	}

	// @PositiveOrZero(message="salary must be positive")
	@Test
	void shouldReturn400WhenSalaryIsZero() throws Exception {

		// Set salary to zero
		employeeRequest.setEmpSalary(new BigDecimal("-1.0"));
		// This will not affect next test as after each test
		// @BeforeEach will create new object every time we call test method

		mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeRequest))).andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false)).andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.errorMessage").value("Validation failed"))
				.andExpect(jsonPath("$.path").value("/api/v1/employees"))
				.andExpect(jsonPath("$.errors.empSalary").value("salary must be positive"))
				.andExpect(jsonPath("$.timestamp").exists());
	}

	// @Min(value=1,message="Performance rating must be at least 1")
	@Test
	void shouldReturn400WhenPerformanceRatingIsLessThan1() throws Exception {

		// Set performance rating > 1
		employeeRequest.setEmpPerformanceRating(0);
		mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeRequest))).andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false)).andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.errorMessage").value("Validation failed"))
				.andExpect(jsonPath("$.path").value("/api/v1/employees"))
				.andExpect(jsonPath("$.errors.empPerformanceRating").value("Performance rating must be at least 1"))
				.andExpect(jsonPath("$.timestamp").exists());
	}

	// @Min(value=1,message="Performance rating must be at least 1")
	@Test
	void shouldReturn400WhenPerformanceRatingIsMoreThan5() throws Exception {

		// Set performance rating > 1
		employeeRequest.setEmpPerformanceRating(6);
		mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeRequest))).andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false)).andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.errorMessage").value("Validation failed"))
				.andExpect(jsonPath("$.path").value("/api/v1/employees"))
				.andExpect(jsonPath("$.errors.empPerformanceRating").value("Performance rating must be up to 5"))
				.andExpect(jsonPath("$.timestamp").exists());
	}

	/***********************************************************************************/

	// GetEmployee
	@Test
	void testGetEmployee() throws Exception {

		// programming our mock service
		// When someone calls getEmployee() with this ID, return our fake employee data.
		when(employeeService.getEmployee(validEmpId)).thenReturn(employeeResponse);

		// Make the HTTP Request
		mockMvc.perform(get("/api/v1/employees/{id}", validEmpId).accept(MediaType.APPLICATION_JSON)).andDo(print()) // Print
																														// the
				.andExpect(status().isCreated()) // Verify HTTP status is 201
				.andExpect(jsonPath("$.data.empId").value(validEmpId)) // Check empId in JSON
				.andExpect(jsonPath("$.data.empName").value(employeeResponse.getEmpName())) // Check name
				.andExpect(jsonPath("$.data.empDesignation").value(employeeResponse.getEmpDesignation()))
				.andExpect(jsonPath("$.data.empExperienceYears").value(employeeResponse.getEmpExperienceYears()))
				.andExpect(jsonPath("$.data.empPerformanceRating").value(employeeResponse.getEmpPerformanceRating()))
				.andExpect(jsonPath("$.data.empSalary").value(employeeResponse.getEmpSalary().doubleValue()))// 100000.00
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.statusCode").value(200)) // This is
				.andExpect(jsonPath("$.message").value("Employee retrieved successfully"))
				.andExpect(jsonPath("$.path").value("/api/v1/employees/" + validEmpId));

		// Double-checking that our controller actually called the service method with
		// the correct employee ID
		verify(employeeService).getEmployee(validEmpId);
	}

	@Test
	void testGetEmployee_NotFound() throws Exception {

		// Non exited employee id
		String invalidId = "rewardlyEmp-20251118-190420-9489";

		// Mock the service to throw EmployeeNotFoundException
		when(employeeService.getEmployee(invalidId)).thenThrow(new EmployeeNotFoundException(invalidId));

		// Make the HTTP Request
		mockMvc.perform(get("/api/v1/employees/{id}", invalidId).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isNotFound()) // 404
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.errorCode").value("EMPLOYEE_NOT_FOUND"))// Fail
				.andExpect(jsonPath("$.errorMessage").value("Employee not found with ID: " + invalidId))
				.andExpect(jsonPath("$.path").value("/api/v1/employees/" + invalidId))
				.andExpect(jsonPath("$.timestamp").exists());
		verify(employeeService).getEmployee(invalidId);

	}

	/**
	 * @throws Exception *********************************************************************************/
	@DisplayName("To return list of all employees")
	@Test
	void testGetAllEmployees() throws Exception {
		
		// programming our mock service
		EmployeeResponse empRes = EmployeeResponse.builder()
				.empId("rewardlyEmp-20251118-190420-9481")
				.empName("Rohit Sharma")
				.empDesignation("Java Developer")
				.empSalary(new BigDecimal("90000.00"))
				.empExperienceYears(new BigDecimal("5.0"))
				.empPerformanceRating(5)
				.build();
		
		List<EmployeeResponse> empResList = new ArrayList();
				empResList.add(employeeResponse);
				empResList.add(empRes);
		
		// When someone calls getEmployee() with this ID, return our fake employee data.
				when(employeeService.getAllEmployees()).thenReturn(empResList);

				// Make the HTTP Request
				mockMvc.perform(get("/api/v1/employees")
						.accept(MediaType.APPLICATION_JSON))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.success").value(true))
						.andExpect(jsonPath("$.statusCode").value(200))
						.andExpect(jsonPath("$.message").value("All employees retrieved successfully"))
						.andExpect(jsonPath("$.path").value("/api/v1/employees"))
						.andExpect(jsonPath("$.timeStamp").exists())
						.andExpect(jsonPath("$.data", hasSize(2)))
						.andExpect(jsonPath("$.data[0].empId").value("rewardlyEmp-20251118-190420-9480"))
						.andExpect(jsonPath("$.data[0].empName").value("Wasim Shaikh"))
						.andExpect(jsonPath("$.data[0].empDesignation").value("Java Developer"))
						.andExpect(jsonPath("$.data[1].empId").value("rewardlyEmp-20251118-190420-9481"))
						.andExpect(jsonPath("$.data[1].empName").value("Rohit Sharma"))
						.andExpect(jsonPath("$.data[1].empDesignation").value("Java Developer"));
						
					
				verify(employeeService, times(1)).getAllEmployees();
		
	}
	
	/*
	 * 
	 */
	@DisplayName("To return empty list when there is not employee")
	@Test
	void shouldReturnEmptyListWhenNoEmployee() throws Exception {
		
		List<EmployeeResponse> empResList = new ArrayList();

		// When someone calls getEmployee() with this ID, return our fake employee data.
				when(employeeService.getAllEmployees()).thenReturn(empResList);

				// Make the HTTP Request
				mockMvc.perform(get("/api/v1/employees")
						.accept(MediaType.APPLICATION_JSON))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.success").value(true))
						.andExpect(jsonPath("$.statusCode").value(200))
						.andExpect(jsonPath("$.message").value("All employees retrieved successfully"))
						.andExpect(jsonPath("$.path").value("/api/v1/employees"))
						.andExpect(jsonPath("$.timeStamp").exists())
						.andExpect(jsonPath("$.data", hasSize(0)))
						;
						
				verify(employeeService, times(1)).getAllEmployees();
	}
	

	/***********************************************************************************/
	@Test
	void testUpdateEmployee() throws Exception {
		// Mock Service
		when(employeeService.updateEmployee(validEmpId, employeeRequest)).thenReturn(employeeResponse);

		//
		String jsonRequest = objectMapper.writeValueAsString(employeeRequest);

		// HTTP call
		mockMvc.perform(put("/api/v1/employees/{id}", validEmpId).contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.data.empId").value(validEmpId))
				.andExpect(jsonPath("$.data.empName").value(employeeResponse.getEmpName()))
				.andExpect(jsonPath("$.data.empDesignation").value(employeeResponse.getEmpDesignation()))
				.andExpect(jsonPath("$.data.empExperienceYears").value(employeeResponse.getEmpExperienceYears()))
				.andExpect(jsonPath("$.data.empPerformanceRating").value(employeeResponse.getEmpPerformanceRating()))
				.andExpect(jsonPath("$.data.empSalary").value(employeeResponse.getEmpSalary().doubleValue()))
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message").value("Employee updated successfully"))
				.andExpect(jsonPath("$.path").value("/api/v1/employees/" + validEmpId));

		// Verify
		verify(employeeService).updateEmployee(validEmpId, employeeRequest);

	}

	// error
	/**
	 * @throws Exception
	 *********************************************************************************/
	@Test
	void testDeleteEmployee() throws Exception {

		// doNothing() -> Service layer is returns nothing
		doNothing().when(employeeService).deleteEmployee(validEmpId);

		// Make the HTTP Request
		mockMvc.perform(delete("/api/v1/employees/{id}", validEmpId).accept(MediaType.APPLICATION_JSON)).andDo(print()) // Print
				.andExpect(status().isNoContent()); // Verify HTTP status is 204
	}

	// error
	/***********************************************************************************/

}
