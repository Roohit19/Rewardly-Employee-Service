package com.rewardly.emp.employeedto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Ignore null fields
public class EmployeeResponse {
	
	private Long empId;
	private String empName;
	private String empDesignation;
	private BigDecimal empSalary;
	private  BigDecimal empExperienceYears;
	private int empPerformanceRating;

}
