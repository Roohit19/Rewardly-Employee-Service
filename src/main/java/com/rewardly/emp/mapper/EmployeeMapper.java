package com.rewardly.emp.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.rewardly.emp.employeedto.EmployeeRequest;
import com.rewardly.emp.employeedto.EmployeeResponse;
import com.rewardly.emp.entity.Employee;


//nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE -> ignores mapping of null value fields
//componentModel = "Spring" -> This allows spring to register implementation of this class as bean. 
// add @Component to implementation class which is available in Target folder. This class is generated after 
//compilation by Mapstruct automatically.
@Mapper(componentModel = "Spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {
	
	//This converts EmployeeRequest DTO -> Employee entity
	//target = "empId", ignore = true => ignores mapping of empId
	@Mapping(target = "empId", ignore = true)
	Employee toEntity(EmployeeRequest request);
	
	//This is converting Entity -> EmployeeResponse DTO
	EmployeeResponse toResponse(Employee employee);
	
	//This converts list of Employee Entity  ->  the list of EmployeeResponse DTO
	List<EmployeeResponse> toResponseList(List<Employee> employees);
	
	//This updates Employee entity -> exiting EmployeeRequest DTO
	//target = "empId", ignore = true => ignores mapping of empId
	//@MappingTarget Employee employee => Does not create new object update the existing one
	@Mapping(target = "empId", ignore = true)
	void updateEntityFromRequest(EmployeeRequest empReq, @MappingTarget Employee employee);
	

}
