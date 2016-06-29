package com.ctosb.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ctosb.core.model.User;
import com.ctosb.core.mybatis.Page;

public interface UserMapper {

	int insert(User user);

	int insertAll(List<User> users);

	int delete(int id);

	int deleteByUserName(String userName);

	List<User> getByUserName(String userName);

	List<User> getByUserName(@Param("userName") String userName, Page page);

	List<User> get(Page page);
}
