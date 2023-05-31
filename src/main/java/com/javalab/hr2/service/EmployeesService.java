package com.javalab.hr2.service;

import java.util.List;

import com.javalab.hr2.vo.Department;
import com.javalab.hr2.vo.EmployeeCommonDto;
import com.javalab.hr2.vo.Employees;

/**
 * 서비스 인터페이스
 */
public interface EmployeesService {

	public List<EmployeeCommonDto> getEmployeesList(Employees emp);
	public Employees getEmployees(Integer employeeId);
	public int insertEmployees(Employees emp);
	public List<Department> getDepartmentList();
	public List<EmployeeCommonDto> getEmployeeByCon(EmployeeCommonDto employeeCommonDto);

}