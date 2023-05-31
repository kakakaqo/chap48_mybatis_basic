package com.javalab.hr2.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.javalab.hr2.vo.Department;
import com.javalab.hr2.vo.EmployeeCommonDto;
import com.javalab.hr2.vo.Employees;

/*
 * [매퍼 인터페이스]
 * - 서비스 Layer와 매퍼xml의 쿼리문을 연결해주는 가교(Bridge) 역할
 * - @Mapper : 이 패키지에 있는 (인터페이스) 중에서 @MApper 어노테이션이
 *   있는 인터페이스만이 진정한 매퍼 인터페이스 라는 선언.
 */
@Mapper
public interface EmployeesMapper {

	// 사원 목록 조회
	public List<EmployeeCommonDto> getEmployeeList(Employees emp);
	
	// 사원 한명 조회
	public Employees getEmployees(Integer employeeId);
	
	// 사원등록
	public int insertEmployees(Employees emp);
	
	// 부서 목록 조회
	public List<Department> getDepartmentList();
	
	// 다양한 조건으로 검색
	public List<EmployeeCommonDto> getEmployeeByCon(EmployeeCommonDto employeeCommonDto);
}
