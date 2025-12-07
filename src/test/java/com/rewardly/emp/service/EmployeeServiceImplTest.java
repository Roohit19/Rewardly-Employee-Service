package com.rewardly.emp.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rewardly.emp.employeedto.EmployeeRequest;
import com.rewardly.emp.employeedto.EmployeeResponse;
import com.rewardly.emp.entity.Employee;
import com.rewardly.emp.exception.EmployeeNotFoundException;
import com.rewardly.emp.exception.InvalidEmployeeDataException;
import com.rewardly.emp.mapper.EmployeeMapper;
import com.rewardly.emp.repository.EmployeeRepository;


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
	
	/*Create*/
	
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
	
	@DisplayName("Test valid Employee ID")
	@Test
	void shouldGenerateValidEmployeeId() {
		
		when(employeeMapper.toEntity(employeeRequest)).thenReturn(employee);
		when(employeeRepository.save(employee)).thenReturn(savedEmployee);
		when(employeeMapper.toResponse(savedEmployee)).thenReturn(employeeResponse);
		
		@SuppressWarnings("unused")
		EmployeeResponse employeeResult = employeeService.createEmployee(employeeRequest);
		
		
		//After request and before saving to the DB 
		ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
		verify(employeeRepository).save(captor.capture());
		
		//
		 Employee empPassed = captor.getValue();
		 String empId = empPassed.getEmpId();
		 
		 assertNotNull(empId, "Generated employee ID should not be null");
		 assertFalse(empId.isBlank(), "Generated employee ID should not be blank");
		 assertEquals(employeeRequest.getEmpName(), empPassed.getEmpName());
		 
		 String regex = "^rewardlyEmp-\\d{8}-\\d{6}-\\d{4}$";
		 assertTrue(empId.matches(regex),
				 	"Generated ID should match the format rewardlyEmp-YYYYMMDD-HHMMSS-XXXX, but was: " 
				 	+ empId);		
	}
	
	@DisplayName("Invalid employee data exception ->  exp > 50")
	@Test
	void createEmployee_ExperienceExceeds50() {
		
		employeeRequest.setEmpExperienceYears(new BigDecimal("51.0"));
		
		
		when(employeeMapper.toEntity(employeeRequest)).thenReturn(employee);
		
		InvalidEmployeeDataException exception = assertThrows(InvalidEmployeeDataException.class, 
				() -> employeeService.createEmployee(employeeRequest));
		
		System.out.println("Exception -> "+ exception.getMessage());
		assertTrue(exception.getMessage().contains("experience must be less than 50"));
		
		verify(employeeRepository , never()).save(any(Employee.class));
		
	}
	
	
//	@DisplayName("Invalid employee data exception ->  exp < 0")
//	@Test
//	void createEmployee_ExperienceNegative() {
//		
//		employeeRequest.setEmpExperienceYears(new BigDecimal("0.0"));
//		
//		
//		when(employeeMapper.toEntity(employeeRequest)).thenReturn(employee);
//		
//		InvalidEmployeeDataException exception = assertThrows(InvalidEmployeeDataException.class, 
//				() -> employeeService.createEmployee(employeeRequest));
//		
//		System.out.println("Exception -> "+ exception.getMessage());
//		//assertTrue(exception.getMessage().contains("experience must be less than 50"));
//		//verify(employeeRepository , never()).save(any(Employee.class));
//		
//	}
	
	
	@DisplayName("Invalid employee data exception ->  performanceRating < 1")
	@Test
	void createEmployee_PerformanceRatingLessThan1() {
		
		employeeRequest.setEmpPerformanceRating(0);
		
		when(employeeMapper.toEntity(employeeRequest)).thenReturn(employee);
		
		InvalidEmployeeDataException exception = assertThrows(InvalidEmployeeDataException.class, 
				() -> employeeService.createEmployee(employeeRequest));
		
		System.out.println("Exception -> "+ exception.getMessage());
		assertTrue(exception.getMessage().contains("Performance rating must be between 1 and 5"));
		
		verify(employeeRepository , never()).save(any(Employee.class));
		
	}
	
	
	@DisplayName("Invalid employee data exception ->  performanceRating > 5")
	@Test
	void createEmployee_PerformanceRatingGreaterThan5() {
		
		employeeRequest.setEmpPerformanceRating(6);
		
		when(employeeMapper.toEntity(employeeRequest)).thenReturn(employee);
		
		InvalidEmployeeDataException exception = assertThrows(InvalidEmployeeDataException.class, 
				() -> employeeService.createEmployee(employeeRequest));
		
		System.out.println("Exception -> "+ exception.getMessage());
		assertTrue(exception.getMessage().contains("Performance rating must be between 1 and 5"));
		
		verify(employeeRepository , never()).save(any(Employee.class));
		
	}
	
	
	/*Get*/
	

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
	
	@DisplayName("Get all employee records successfully")
	@Test
	void testGetAllEmployees_success() {
		
		EmployeeResponse empRes = EmployeeResponse
								.builder()
								.empId("rewardlyEmp-20251118-190420-9481")
								.empName("Rohit Sharma")
								.empDesignation("Java Developer")
								.empSalary(new BigDecimal("500000.00"))
								.empExperienceYears(new BigDecimal("6.0"))
								.empPerformanceRating(5)
								.build();
		
		Employee emp = Employee
								.builder()
								.empId("rewardlyEmp-20251118-190420-9481")
								.empName("Rohit Sharma")
								.empDesignation("Java Developer")
								.empSalary(new BigDecimal("500000.00"))
								.empExperienceYears(new BigDecimal("6.0"))
								.empPerformanceRating(5)
								.build();
		
		List<EmployeeResponse> empResList = Arrays.asList(employeeResponse,empRes);
		List<Employee> empList = Arrays.asList(employee,emp);
			
		when(employeeRepository.findAll()).thenReturn(empList);
		when(employeeMapper.toResponseList(empList)).thenReturn(empResList);
		
		List<EmployeeResponse> result = employeeService.getAllEmployees();
		
		assertNotNull(result);
		
		assertEquals(2, result.size());
		
		assertEquals("rewardlyEmp-20251118-190420-9480", result.get(0).getEmpId());
		assertEquals("rewardlyEmp-20251118-190420-9481", result.get(1).getEmpId());
		
		verify(employeeRepository, times(1)).findAll();
		verify(employeeMapper, times(1)).toResponseList(empList);
			
	}
	
	@DisplayName("Returns empty list")
	@Test
	void getAllEmployee_EmptyList() {
		
		when(employeeRepository.findAll()).thenReturn(Arrays.asList());
		when(employeeMapper.toResponseList(anyList())).thenReturn(Arrays.asList());
		
		List<EmployeeResponse> result = employeeService.getAllEmployees();
		
		assertNotNull(result);
		assertTrue(result.isEmpty());
		
		verify(employeeRepository, times(1)).findAll();
		verify(employeeMapper, times(1)).toResponseList(Arrays.asList());	
		
	}
	
	
	
	//Wasim
	@Test
	@DisplayName("Should update employee successfully")
	void testUpdateEmployee_success() {
		//Arrange
		EmployeeRequest updateRequest=new EmployeeRequest();
		updateRequest.setEmpName("Sachin Reddy");
		updateRequest.setEmpDesignation("Senior Software Engineer");
		updateRequest.setEmpSalary(new BigDecimal("90000.00"));
		updateRequest.setEmpExperienceYears(new BigDecimal("6.0"));
		updateRequest.setEmpPerformanceRating(5);
		
		when(employeeRepository.findById(validEmpId)).thenReturn(Optional.of(employee));

		//		doNothing().when(employeeMapper).updateEntityFromRequest(updateRequest, employee);

		//Print when Mapper updates entity(like doPrint())
		
		doAnswer(invocation->{
			EmployeeRequest requestPrint=invocation.getArgument(0);
			Employee employeePrint=invocation.getArgument(1);
			System.out.println("Update Mapper called");
			System.out.println("Update Request: "+requestPrint);
			System.out.println("Employee Before update: "+employeePrint);
			
		      // --- simulate mapper behaviour ---
			employeePrint.setEmpName(requestPrint.getEmpName());
			employeePrint.setEmpDesignation(requestPrint.getEmpDesignation());
			employeePrint.setEmpSalary(requestPrint.getEmpSalary());
			employeePrint.setEmpExperienceYears(requestPrint.getEmpExperienceYears());
			employeePrint.setEmpPerformanceRating(requestPrint.getEmpPerformanceRating());

	        System.out.println("Employee AFTER update: " + employeePrint);
	        
			return null;
		}).when(employeeMapper).updateEntityFromRequest(updateRequest, employee);
		
		//		when(employeeRepository.save(employee)).thenReturn(employee);
		// Print when repository saves employee
		when(employeeRepository.save(employee)).thenAnswer(invocation->{
			Employee savedEmployeePrint=invocation.getArgument(0);
			System.out.println("Repository save called");
			System.out.println("Employee being saved: "+savedEmployeePrint);
			return savedEmployeePrint;
		});
		
		 // Build response from the saved employee argument
	    when(employeeMapper.toResponse(any(Employee.class))).thenAnswer(invocation -> {
	        Employee emp = invocation.getArgument(0);
	        EmployeeResponse resp = new EmployeeResponse();
	        resp.setEmpId(emp.getEmpId());
	        resp.setEmpName(emp.getEmpName());
	        resp.setEmpDesignation(emp.getEmpDesignation());
	        resp.setEmpSalary(emp.getEmpSalary());
	        resp.setEmpExperienceYears(emp.getEmpExperienceYears());
	        resp.setEmpPerformanceRating(emp.getEmpPerformanceRating());
	        return resp;
	    });
		
		//Act
		EmployeeResponse updateEmployeeResult = employeeService.updateEmployee(validEmpId, updateRequest);
		
		//Print final response
		System.out.println("Final service response");
		System.out.println("Employee Response: "+updateEmployeeResult);
		
		// Assert
	    assertNotNull(updateEmployeeResult);
	    assertEquals("Sachin Reddy", updateEmployeeResult.getEmpName());
	    assertEquals("Senior Software Engineer", updateEmployeeResult.getEmpDesignation());
	    assertEquals(new BigDecimal("90000.00"), updateEmployeeResult.getEmpSalary());
	    assertEquals(new BigDecimal("6.0"), updateEmployeeResult.getEmpExperienceYears());

	    verify(employeeRepository, times(1)).findById(validEmpId);
	    verify(employeeMapper, times(1)).updateEntityFromRequest(any(EmployeeRequest.class), any(Employee.class));
	    verify(employeeRepository, times(1)).save(any(Employee.class));
	    verify(employeeMapper, times(1)).toResponse(any(Employee.class));
		
		
		
	}

	//Amol
	/* Delete test cases*/
	@DisplayName("Delete success case")
	@Test
	void testDeleteEmployee() {
		
		//Calling the findById()
		when(employeeRepository.findById(validEmpId)).thenReturn(Optional.of(employee));
		
		//Delete is void so doNothing()
		doNothing().when(employeeRepository).delete(employee);
		
		//call service method
		employeeService.deleteEmployee(validEmpId);
		
		verify(employeeRepository, times(1)).findById(validEmpId);
		verify(employeeRepository, times(1)).delete(employee);						
	}
	
	
	@DisplayName("Delete employee: should throw when ID not found")
	@Test
	void testDeleteEmployee_NotFound() {
		
		//Calling the findById() -> empty object		
		when(employeeRepository.findById(invalidId)).thenReturn(Optional.empty());
		
		//throwError		
		EmployeeNotFoundException excption = assertThrows(EmployeeNotFoundException.class, 
					() -> employeeService.deleteEmployee(invalidId) );
		
		//verify message 		
		assertEquals("Employee not found with ID: "+invalidId ,excption.getMessage());		
		//System.out.println(excption.getMessage());
		
		//Verify number of hits		
		verify(employeeRepository, times(1)).findById(invalidId);
		verify(employeeRepository, never()).delete(any());		
	}
	

}
