package com.jy23.mapper;

import org.apache.ibatis.annotations.Param;

public interface BaseMapper {
	void creatASKHis(@Param("tableName") String tableName);
}
