package com.ctosb.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ctosb.core.model.User;
import com.ctosb.core.mybatis.page.Limit;
import com.ctosb.core.mybatis.page.Page;
import com.ctosb.core.mybatis.page.PageList;
import com.ctosb.core.mybatis.sort.Sort;

public interface UserMapper {

	int insert(User user);

	int insertAll(List<User> users);

	int delete(int id);

	int deleteByUserName(String userName);

	List<User> getByUserName(String userName);

	PageList<User> getByUserName(String userName, Page page);
	
	PageList<User> getByUserName(String userName, Page page,Sort... sorts);

	List<User> getByUserName(@Param("userName") String userName, Limit limit);

	List<User> get(Page page,Sort... sorts);
}
