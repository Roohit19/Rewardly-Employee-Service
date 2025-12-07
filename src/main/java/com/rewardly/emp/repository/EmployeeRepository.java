package com.rewardly.emp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rewardly.emp.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

	Optional<Employee> findByEmpName(String string);

}
