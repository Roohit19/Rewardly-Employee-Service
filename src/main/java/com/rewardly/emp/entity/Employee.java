package com.rewardly.emp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int empId;

	@Column(nullable = false, name = "emp_name")
	private String empName;

	@Column(nullable = false, name = "emp_designation")
	private String empDesignation;

	@Column(nullable = false, name = "emp_salary")
	private double empSalary;

	@Column(nullable = false, name = "emp_experience")
	private double empExperience;

	@Column(nullable = false, name = "emp_performance_rating")
	private int empPerformanceRating;

}
