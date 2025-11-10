package com.rewardly.emp.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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

// @Data generates equals() and hashCode() methods based on all non-static fields by default.
//  This can cause issues in JPA entities if the ID is auto-generated,
// as two different persisted entities with identical field values (but different IDs) 
// may be considered equal.

// Override equals() and hashCode() manually based on the ID only.
// Alternatively, use Lombok’s @EqualsAndHashCode(onlyExplicitlyIncluded = true) 
// and mark the ID field with @EqualsAndHashCode.Include.
// Avoid using @Data directly on JPA entities; prefer @Getter, @Setter, and explicit equals/hashCode methods.


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder=true)
@Table(name="employees", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "designation"})
    },
indexes= {@Index(name="idx_employee_designation",columnList="designation")})
public class Employee {
	// Follow database naming conventions:
// If the table name is "employee", use column names like "name" instead of "emp_name".
// Add indexes on frequently queried columns to improve query performance.
//
// Note:
//  In MySQL, GenerationType.AUTO behaves like GenerationType.IDENTITY.
//  GenerationType.AUTO creates a separate sequence table internally.
//  GenerationType.IDENTITY uses the database's native auto-increment feature.


	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id",updatable = false, nullable = false)
	private String empId;

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

// For salary, monetary values, or scientific computations,
// use BigDecimal to maintain precision and avoid floating-point rounding errors.
// Use double only when approximate values are acceptable and precision is not critical.

	
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
