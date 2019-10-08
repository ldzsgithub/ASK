package com.jy23.server;

import java.util.List;

import com.jy23.entity.Department;
import com.jy23.entity.Users;

public interface UsersServer {

	int checkLogin(Users users);
	
	int registerCheckUsers(String users);

	void insertUsers(Users user);

	List<Users> findAllUsers(Users user);

	void deleteUsers(String id);

	void updateUsers(Users user);

	int findLoginflag(String nameId);

	void updateQx(Users user);

	Users findUserById(String id);

	Department selectAuthority(Integer departmentId);

	List<Users> findUsersByAuthority(String departmentId);
}
