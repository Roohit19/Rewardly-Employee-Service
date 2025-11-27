package com.rewardly.emp.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.rewardly.emp.employeedto.EmployeeResponse;
import com.rewardly.emp.service.EmployeeServiceImpl;

// This is a Slice test (Neither pure unit test nor 
@WebMvcTest(EmployeeController.class) // Set up test environment that include only web layer
class EmployeeControllerTest {
	
	@Autowired
	private MockMvc mockMvc; //  test bot that can mock HTTP requests to the controller
	
	@MockBean	// Creates a mock version of the service layer
	private EmployeeServiceImpl employeeService;
	

//	@Test
//	void testCreateEmployee() {
//		fail("Not yet implemented");
//		
//	}
//
	@Test
	void testGetEmployee() throws Exception {
	
		
		//Creating a mock employee object that represents what the service would return
		
		String validEmpId = "rewardlyEmp-20251118-190420-9480";
		EmployeeResponse employeeResponse = EmployeeResponse.builder()
				.empId(validEmpId)
				.empName("Wasim Shaikh")
				.empDesignation("Java Developer")
				.empSalary(new BigDecimal("100000.00"))
				.empExperienceYears(new BigDecimal("3.0"))
				.empPerformanceRating(5)
				.build();
		
		//programming our mock service
		//When someone calls getEmployee() with this ID, return our fake employee data.
		when(employeeService.getEmployee(validEmpId)).thenReturn(employeeResponse);
		
		//Make the HTTP Request
		mockMvc.perform(get("/api/v1/employees/{id}", validEmpId).accept(MediaType.APPLICATION_JSON))
		.andDo(print())  // Print the response (helpful for debugging)
		.andExpect(status().isCreated())  // Verify HTTP status is 201
		.andExpect(jsonPath("$.data.empId").value(validEmpId)) // Check empId in JSON
		.andExpect(jsonPath("$.data.empName").value(employeeResponse.getEmpName())) // Check name
		.andExpect(jsonPath("$.data.empDesignation").value(employeeResponse.getEmpDesignation()))
		.andExpect(jsonPath("$.data.empExperienceYears").value(employeeResponse.getEmpExperienceYears()))
		.andExpect(jsonPath("$.data.empPerformanceRating").value(employeeResponse.getEmpPerformanceRating()))
		.andExpect(jsonPath("$.data.empSalary").value(employeeResponse.getEmpSalary().doubleValue()))//100000.00 Json = 100000.0
		//.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		//.andExpect((ResultMatcher) content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.success").value(true))
		.andExpect(jsonPath("$.statusCode").value(200)) // This is 200
		.andExpect(jsonPath("$.message").value("Employee retrieved successfully"))
		.andExpect(jsonPath("$.path").value("/api/v1/employees/"+validEmpId));
		
		
		//Double-checking that our controller actually called the service method with 
		//the correct employee ID
		verify(employeeService).getEmployee(validEmpId);	
		
	}
//
//	@Test
//	void testGetAllEmployees() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testUpdateEmployee() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testDeleteEmployee() {
//		fail("Not yet implemented");
//	}

}
