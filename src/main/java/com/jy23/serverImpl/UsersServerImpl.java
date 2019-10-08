package com.jy23.serverImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jy23.entity.Department;
import com.jy23.entity.Users;
import com.jy23.mapper.DepartmentMapper;
import com.jy23.mapper.UsersMapper;
import com.jy23.server.UsersServer;

@Service("usersServer")
public class UsersServerImpl implements UsersServer{
	@Autowired
	UsersMapper usersMapper;

	@Override
	public int checkLogin(Users users) {
		return usersMapper.checkLogin(users);
	}

	@Override
	public int registerCheckUsers(String users) {
		return usersMapper.registerCheckUsers(users);
	}

	@Override
	public void insertUsers(Users user) {
		usersMapper.insertUsers(user);
	}

	@Override
	public List<Users> findAllUsers(Users user) {
		return usersMapper.findAllUsers(user);
	}

	@Override
	public void deleteUsers(String id) {
		usersMapper.deleteUsers(id);
	}

	@Override
	public void updateUsers(Users user) {
		usersMapper.updateUsers(user);
	}

	@Override
	public int findLoginflag(String nameId) {
		return usersMapper.findLoginflag(nameId);
	}

	@Override
	public void updateQx(Users user) {
		usersMapper.updateQx(user);
	}

	@Override
	public Users findUserById(String id) {
		return usersMapper.findUserById(id);
	}
	
	@Autowired
	DepartmentMapper departmentMapper;
	@Override
	public Department selectAuthority(Integer departmentId) {
		return departmentMapper.selectAuthority(departmentId);
	}

	@Override
	public List<Users> findUsersByAuthority(String departmentId) {
		return usersMapper.findUsersByAuthority(departmentId);
	}

}
