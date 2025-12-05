package com.rewardly.emp.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rewardly.emp.employeedto.EmployeeRequest;
import com.rewardly.emp.employeedto.EmployeeResponse;
import com.rewardly.emp.entity.Employee;
import com.rewardly.emp.exception.EmployeeNotFoundException;
import com.rewardly.emp.mapper.EmployeeMapper;
import com.rewardly.emp.repository.EmployeeRepository;

import jakarta.inject.Inject;

//Annotate EmployeeServiceImplTest
//Controller call
//Mock repository 
//Verify 
@DisplayName("Employee service unit test")
@ExtendWith(MockitoExtension.class) 
class EmployeeServiceImplTest {
	
	@Mock
	EmployeeRepository employeeRepository;
	
	@Mock
	EmployeeMapper employeeMapper;
	
	@InjectMocks
	EmployeeServiceImpl employeeService;
	
	EmployeeRequest employeeRequest;
	EmployeeResponse employeeResponse;
	Employee employee;
	Employee savedEmployee;
	String validEmpId;
	// Non exited employee id
	String invalidId = "rewardlyEmp-20251118-190420-9489";
	
	@BeforeEach
	void setUp() {
		
		validEmpId = "rewardlyEmp-20251118-190420-9480";
		employeeResponse = EmployeeResponse
								.builder()
								.empId(validEmpId)
								.empName("Wasim Shaikh")
								.empDesignation("Java Developer")
								.empSalary(new BigDecimal("100000.00"))
								.empExperienceYears(new BigDecimal("3.0"))
								.empPerformanceRating(5)
								.build();

		employeeRequest = EmployeeRequest
							.builder()
							.empName("Wasim Shaikh")
							.empDesignation("Java Developer")
							.empSalary(new BigDecimal("100000.00"))
							.empExperienceYears(new BigDecimal("3.0"))
							.empPerformanceRating(5)
							.build();
		
		employee = Employee
						.builder()
						.empId("rewardlyEmp-20251118-190420-9480")
						.empName("Wasim Shaikh")
						.empDesignation("Java Developer")
						.empSalary(new BigDecimal("100000.00"))
						.empExperienceYears(new BigDecimal("3.0"))
						.empPerformanceRating(5)
						.build();
		
		savedEmployee = Employee
							.builder()
							.empId("rewardlyEmp-20251118-190420-9480")
							.empName("Wasim Shaikh")
							.empDesignation("Java Developer")
							.empSalary(new BigDecimal("100000.00"))
							.empExperienceYears(new BigDecimal("3.0"))
							.empPerformanceRating(5)
							.build();		
	}
	
	@DisplayName("Creating employee with valid data")
	@Test
	void testCreateEmployee() {
		//Request -> Entity
		when(employeeMapper.toEntity(employeeRequest)).thenReturn(employee);
		//Saving in database
		when(employeeRepository.save(employee)).thenReturn(savedEmployee);
		//Employee -> Response
		when(employeeMapper.toResponse(savedEmployee)).thenReturn(employeeResponse);
		
		
		//Calling service method
		EmployeeResponse employeeResult = employeeService.createEmployee(employeeRequest);
		
		assertNotNull(employeeResult);
		
		assertEquals(employeeResponse.getEmpName(), employeeResult.getEmpName());
		assertEquals(employeeResponse.getEmpDesignation(), employeeResult.getEmpDesignation());
		assertEquals(employeeResponse.getEmpExperienceYears(), employeeResult.getEmpExperienceYears());
		assertEquals(employeeResponse.getEmpPerformanceRating(), employeeResult.getEmpPerformanceRating());
		assertEquals(employeeResponse.getEmpSalary(), employeeResult.getEmpSalary());
		assertEquals(employeeResponse.getEmpId(), employeeResult.getEmpId());
		
		verify(employeeRepository, times(1)).save(employee);
		verify(employeeMapper, times(1)).toEntity(employeeRequest);
		verify(employeeMapper, times(1)).toResponse(savedEmployee);
				
	}

	@DisplayName("Get employee by ID")
	@Test
	void testGetEmployee() {
		//Hitting database
		when(employeeRepository.findById(validEmpId)).thenReturn(Optional.of(employee));
		//Calling mapper
		when(employeeMapper.toResponse(employee)).thenReturn(employeeResponse);
		//Calling getEmployee()
		EmployeeResponse employeeResult = employeeService.getEmployee(validEmpId);
		//Verify with provided and returning
		assertNotNull(employeeResult);
		assertEquals(validEmpId, employeeResult.getEmpId());
		assertEquals(employeeResponse.getEmpDesignation(), employeeResult.getEmpDesignation());
		assertEquals(employeeResponse.getEmpSalary(), employeeResult.getEmpSalary());
		assertEquals(employeeResponse.getEmpName(), employeeResult.getEmpName());
		
		//Verifying how many times we are calling the methods 
		//also checking if mock is working 
		verify(employeeRepository, times(1)).findById(validEmpId);
		verify(employeeMapper, times(1)).toResponse(employee);
	}
	
	@DisplayName("Employee not found exception")
	@Test
	void getEmployee_ShouldThrowExceptionWhenEmployeeDoesNotExist(){
		
		when(employeeRepository.findById(invalidId)).thenReturn(Optional.empty());
		
		EmployeeNotFoundException assertThrows = assertThrows(EmployeeNotFoundException.class, 
				() -> {employeeService.getEmployee(invalidId);} );
		
		assertEquals("Employee not found with ID: "+ invalidId, assertThrows.getMessage());
		
		verify(employeeRepository, times(1)).findById(invalidId);
		
	}
	
	
	@DisplayName("")
	@Test
	shouldGenerateValidEmployeeId
	
	

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
//
//	@Test
//	void testEmployeeServiceImpl() {
//		fail("Not yet implemented");
//	}

}
