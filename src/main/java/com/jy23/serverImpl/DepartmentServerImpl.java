package com.jy23.serverImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jy23.entity.Area;
import com.jy23.entity.Department;
import com.jy23.mapper.DepartmentMapper;
import com.jy23.server.DepartmentServer;

import java.util.ArrayList;
import java.util.List;

@Service("departmentServer")
public class DepartmentServerImpl implements DepartmentServer {
	@Autowired
	private DepartmentMapper departmentMapper;

	@Override
	public boolean insertSelective(Department department){
		int num = departmentMapper.insertSelective(department);
		return num > 0;
	}

	@Override
	public boolean updateSelective(Department department){
		int num = departmentMapper.updateSelective(department);
		return num > 0;
	}

	@Override
	public boolean deleteByPrimaryKey(Integer departmentId){
		int num = departmentMapper.deleteByPrimaryKey(departmentId);
		return num > 0;
	}

	@Override
	public Department findByPrimaryKey(Integer departmentId){
		return departmentMapper.findByPrimaryKey(departmentId);
	}

	@Override
	public List<Department> findDepartmentSelective(Department department){ 
		List<Department> list = departmentMapper.findDepartmentSelective(department);
		return list == null ? new ArrayList<Department>() : list;
	}

	@Override
	public int countArea(Integer departmentId) {
		return departmentMapper.countArea(departmentId);
	}

	@Override
	public List<Area> findArea(Integer departmentId) {
		return departmentMapper.findArea(departmentId);
	}

}
