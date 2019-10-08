package com.jy23.entity;

import lombok.Data;

@Data
public class Users {
	private String id;			//
	private String name;		//用户名
	private String password;	//密码
	
	private String phoneNumber;	//手机号
	
	private String authority;	//权限部门id
	private Integer loginflag;	//1超级管理员 2管理员 3普通用户
}
