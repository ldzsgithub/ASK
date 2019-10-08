package com.jy23.entity ;

import java.lang.String;
import java.lang.Integer;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Department {
	
	private Integer departmentId;			//部门id
	private String departmentName;			//部门名字
	private String departmentRemarks;		//部门备注
	private Integer companyId;				//公司id

}
