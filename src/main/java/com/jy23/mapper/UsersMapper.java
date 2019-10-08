package com.jy23.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jy23.entity.Users;

public interface UsersMapper {
	
	int registerCheckUsers(@Param("users")String users);

	int checkLogin(Users users);

	void insertUsers(Users user);

	List<Users> findAllUsers(Users user);

	void deleteUsers(@Param("id")String id);

	void updateUsers(Users user);

	int findLoginflag(@Param("nameId")String nameId);

	void updateQx(Users user);

	Users findUserById(@Param("id")String id);

	List<Users> findUsersByAuthority(@Param("departmentId")String departmentId);

}
