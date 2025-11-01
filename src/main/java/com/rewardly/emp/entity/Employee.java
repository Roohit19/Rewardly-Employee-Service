package com.rewardly.emp.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
//import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
//@Data equal() and hashcode() object equivalent based on all fields
//two entities with same data and different Ids ,they'll be equal
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder=true)
@Table(name="employees",
indexes= {@Index(name="idx_employee_designation",columnList="designation")})
public class Employee {
	//Keep it column name database friendly like Table="employee" then
	//column should be column="name"(not emp_name) according to standards
	//add indexes on frequently used fields for database optimization
	//In Mysql GenerationType.AUTO behaves like GenerationType.IDENTITY

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id",updatable = false, nullable = false)
	private Long empId;

	@Column(nullable = false, name = "name")
	@NotBlank(message="Employee name is required")
	@Size(min=2,max=100,message="Name must be between 2 and 100 characters")
	@Pattern(regexp="^[A-Za-z]+( [A-Za-z]+)*$",message="Name must contain only letters and spaces")
	private String empName;
	
	@Size(min=2,max=100,message="Name must be between 2 and 100 characters")
	@Pattern(regexp="^[A-Za-z]+( [A-Za-z]+)*$",message="Name must contain only letters and spaces")
	@NotBlank(message="Designation is required")
	@Column(nullable = false, name = "designation",length=100)
	private String empDesignation;

	//For salary and money/currency, scientific calculations 
	//use BigDecimal for approxValue with less precision 
	
//	@DecimalMin(value="0.0",inclusive=false,message="Salary must be greater than zero")
	@Column(nullable = false, name = "salary",precision=10,scale=2)
	@PositiveOrZero(message="salary must be positive")
	private BigDecimal empSalary;

	@PositiveOrZero(message="Experience must be positive")
	@Column(nullable = false, name = "experience_years",precision=3,scale=1)
	private  BigDecimal empExperienceYears;

	@Min(value=1,message="Performance rating must be at least 1")
	@Max(value=5,message="Performance rating must be up to 5")
	@Column(nullable = false, name = "performance_rating")
	private int empPerformanceRating;
	
	

}
