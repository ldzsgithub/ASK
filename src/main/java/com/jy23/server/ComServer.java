package com.jy23.server;

import java.util.List;

import com.jy23.entity.Com;

public interface ComServer {
	boolean insertSelective(Com com);

	boolean updateSelective(Com com);

	boolean deleteByPrimaryKey(Integer comId);

	Com findByPrimaryKey(Integer comId);

	List<Com> findComSelective(Com com);

	List<Com> findComSelectivePage(Com com ,Integer pageSize ,Integer pageNo);
}
