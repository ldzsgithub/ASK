package com.jy23.server;

import java.util.List;

import com.jy23.entity.Area;
import com.jy23.entity.Department;

public interface DepartmentServer {
	boolean insertSelective(Department department);

	boolean updateSelective(Department department);

	boolean deleteByPrimaryKey(Integer departmentId );

	Department findByPrimaryKey(Integer departmentId );

	List<Department> findDepartmentSelective(Department department);

	int countArea(Integer departmentId);

	List<Area> findArea(Integer departmentId);
}
