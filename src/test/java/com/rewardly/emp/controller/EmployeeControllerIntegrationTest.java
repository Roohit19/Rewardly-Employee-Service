package com.rewardly.emp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewardly.emp.employeedto.EmployeeRequest;
import com.rewardly.emp.employeedto.EmployeeResponse;
import com.rewardly.emp.entity.Employee;
import com.rewardly.emp.repository.EmployeeRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // this will follow the configuration from application-test
@Transactional
@DisplayName("Controller integration test")

public class EmployeeControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private EmployeeRepository employeeRepository;

	private EmployeeRequest employeeRequest;

	@BeforeEach
	private void setUp() {

		employeeRequest = EmployeeRequest.builder().empName("Rohit Sharma").empDesignation("Java Developer")
				.empExperienceYears(new BigDecimal("6.0")).empSalary(new BigDecimal("100000.0")).empPerformanceRating(5)
				.build();

	}

	@DisplayName("Should create employee successfully and return 201 with full response body")
	@Test
	void testCreateEmployee_Success() throws Exception {
		//Mock controller
		mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeRequest))).andDo(print())
				.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.statusCode").value(201))
				.andExpect(jsonPath("$.message").value("Employee created successfully"))
				.andExpect(jsonPath("$.timeStamp").isNotEmpty()) // check is that it is not null
				.andExpect(jsonPath("$.data.empId").isNotEmpty()) // Check
				.andExpect(jsonPath("$.data.empName").value(employeeRequest.getEmpName())) // Check name
				.andExpect(jsonPath("$.data.empDesignation").value(employeeRequest.getEmpDesignation()))
				.andExpect(jsonPath("$.data.empExperienceYears")
						.value(employeeRequest.getEmpExperienceYears().doubleValue()))
				.andExpect(jsonPath("$.data.empPerformanceRating").value(employeeRequest.getEmpPerformanceRating()))
				.andExpect(jsonPath("$.data.empSalary").value(employeeRequest.getEmpSalary().doubleValue()))// 100000.00
				.andExpect(jsonPath("$.path").value("/api/v1/employees"));

		// EXTRA: Verify saved in DB
		Employee saved = employeeRepository.findByEmpName("Rohit Sharma").get();
		assertEquals("Rohit Sharma", saved.getEmpName());
	}

	@DisplayName("Should return 400 when employee name is missing")
	@Test
	void testCreateEmployee_ShouldReturnBadRequest() throws Exception {

		employeeRequest.setEmpName(null);

		mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeRequest))).andDo(print())
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.errorMessage").value("Validation failed"));

		List<Employee> all = employeeRepository.findAll();
		assertTrue(all.size() == 0);
	}

	/* Get */

	@DisplayName("Get employee by id successfully")
	@Test
	void testGetEmployeeById_Success() throws Exception {

		MvcResult result = mockMvc
				.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(employeeRequest)))
				.andExpect(status().isCreated()).andReturn();

		String responseBody = result.getResponse().getContentAsString();

		String empId = objectMapper.readTree(responseBody).path("data").path("empId").asText();

		mockMvc.perform(get("/api/v1/employees/{id}", empId)).andDo(print()).andExpect(status().isCreated())
				.andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message").value("Employee retrieved successfully"))
				.andExpect(jsonPath("$.timeStamp").isNotEmpty()) // check is that it is not null
				.andExpect(jsonPath("$.data.empId").isNotEmpty()) // Check
				.andExpect(jsonPath("$.data.empName").value(employeeRequest.getEmpName())) // Check name
				.andExpect(jsonPath("$.data.empDesignation").value(employeeRequest.getEmpDesignation()))
				.andExpect(jsonPath("$.data.empExperienceYears")
						.value(employeeRequest.getEmpExperienceYears().doubleValue()))
				.andExpect(jsonPath("$.data.empPerformanceRating").value(employeeRequest.getEmpPerformanceRating()))
				.andExpect(jsonPath("$.data.empSalary").value(employeeRequest.getEmpSalary().doubleValue()))// 100000.00
				.andExpect(jsonPath("$.path").value("/api/v1/employees/" + empId));

	}

	/* GetAll */
	@DisplayName("Get all")
	@Test
	void testGetAllEmployees_Success() throws Exception {

	}

	/* Update - Amol */
	@DisplayName("Update")
	@Test
	void testUpdateEmployee_Success() throws Exception {

		// Create employee

		MvcResult createResult = mockMvc.perform(post("/api/v1/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeRequest)))
				.andExpect(status().isCreated()).andReturn();

		// find employee -> get employee object
		
		String json = createResult.getResponse().getContentAsString();
		JsonNode node = objectMapper.readTree(json);
		String empId  = node.path("data").path("empId").asText();
		
		// map
		
		EmployeeRequest updatedEmployee = EmployeeRequest.builder()
											.empName("Rohit Varma")
											.empDesignation("Senior Java Developer")
											.empExperienceYears(new BigDecimal("7.0"))
											.empPerformanceRating(5)
											.empSalary(new BigDecimal("120000.0"))
											.build();
		// verify
		
		mockMvc.perform(put("/api/v1/employees/{id}", empId)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(updatedEmployee)))
	            .andDo(print())
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.success").value(true))
	            .andExpect(jsonPath("$.statusCode").value(200))
	            .andExpect(jsonPath("$.message").value("Employee updated successfully"))
	            .andExpect(jsonPath("$.path").value("/api/v1/employees/" + empId))
	            .andExpect(jsonPath("$.data.empId").value(empId))
	            .andExpect(jsonPath("$.data.empName").value(updatedEmployee.getEmpName()))
	            .andExpect(jsonPath("$.data.empDesignation").value(updatedEmployee.getEmpDesignation()))
	            .andExpect(jsonPath("$.data.empExperienceYears").value(7.0))
	            .andExpect(jsonPath("$.data.empSalary").value(updatedEmployee.getEmpSalary()))
	            .andExpect(jsonPath("$.data.empPerformanceRating").value(5));
		
		// Verify DB saved
	    Employee saved = employeeRepository.findById(empId).orElseThrow();
	    
	    //Verify actuall read from the DB
	    
	    assertEquals(updatedEmployee.getEmpName(), saved.getEmpName());
	    assertEquals(updatedEmployee.getEmpDesignation(), saved.getEmpDesignation());
	    assertEquals(updatedEmployee.getEmpExperienceYears(), saved.getEmpExperienceYears());
	    assertEquals(new BigDecimal("120000.0"), saved.getEmpSalary());
	    assertEquals(updatedEmployee.getEmpPerformanceRating(),saved.getEmpPerformanceRating());
	    

	}

//	/*Delete - Wasim*/
//	@DisplayName("Delete")
//	@Test
//	void testDeleteEmployee_Success() throws Exception {
//	
//	}

	// Preparation for frontend development -> Angular Basic commands
	// Revise
	// GetAll

}
