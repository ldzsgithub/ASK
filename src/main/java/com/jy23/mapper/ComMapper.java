package com.jy23.mapper ;

import org.apache.ibatis.annotations.Param;

import com.jy23.entity.Com;

import java.util.List;

public interface ComMapper {
	int insertSelective(Com com);

	int updateSelective(Com com);

	int deleteByPrimaryKey(@Param("comId")Integer comId);

	Com findByPrimaryKey(@Param("comId")Integer comId);

	List<Com> findSelective(Com com);

}
