package com.rewardly.emp.employeedto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {
	
	@NotBlank(message="Employee name is required")
	@Size(min=2,max=100,message="Name must be between 2 and 100 characters")
	@Pattern(regexp="^[A-Za-z]+( [A-Za-z]+)*$",message="Name must contain only letters and spaces")
	private String empName;
	
	@Size(min=2,max=100,message="Name must be between 2 and 100 characters")
	@Pattern(regexp="^[A-Za-z]+( [A-Za-z]+)*$",message="Name must contain only letters and spaces")
	@NotBlank(message="Designation is required")
	private String empDesignation;
	
	
	@PositiveOrZero(message="salary must be positive")
	private BigDecimal empSalary;
	
	@DecimalMin(value="1.0", message="Experience years must be at least 0")
	@DecimalMax(value="50.0", message="Experience years must be up to 50")
	private  BigDecimal empExperienceYears;
	
	@Min(value=1,message="Performance rating must be at least 1")
	@Max(value=5,message="Performance rating must be up to 5")
	private Integer empPerformanceRating;
	

}
/*
 * {"success":true,
 * "data":
 * 		{	"empId":1,
 * 			"empName":"Riya Singh",
 * 			"empDesignation":"Manager",
 * 			"empSalary":92000,
 * 			"empExperienceYears":6,
 * 			"empPerformanceRating":2
 * 		},
 * "message":"Employee created successfully",
 * "path":"/employees",
 * "statusCode":201,
 * "timeStamp":"2025-11-04T17:57:29"
 * }
*C:\Users\Amol Gadkari>
**/
