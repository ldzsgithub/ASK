package com.jy23.mapper ;

import org.apache.ibatis.annotations.Param;

import com.jy23.entity.Area;
import com.jy23.entity.Department;

import java.util.List;

public interface DepartmentMapper {
	int insertSelective( Department department );

	int updateSelective( Department department );

	int deleteByPrimaryKey(@Param("department_id") Integer departmentId );

	Department findByPrimaryKey(@Param("department_id") Integer departmentId );

	List<Department> findDepartmentSelective( Department department );

	Department selectAuthority(@Param("department_id")Integer departmentId);

	int countArea(Integer departmentId);

	List<Area> findArea(Integer departmentId);

}
