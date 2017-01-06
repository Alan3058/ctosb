package com.ctosb.core;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ctosb.core.mapper.UserMapper;
import com.ctosb.core.model.User;
import com.ctosb.core.mybatis.page.Limit;
import com.ctosb.core.mybatis.page.Page;
import com.ctosb.core.mybatis.sort.Sort;
import com.ctosb.core.mybatis.sort.SortType;

public class SpringMybatisTest {

	ApplicationContext applicationContext;

	@Before
	public void init() {
		applicationContext = new ClassPathXmlApplicationContext("spring-mybatis.xml");
	}

	@Test
	public void testInsert() {
		UserMapper userMapper = applicationContext.getBean(UserMapper.class);
		User user = new User().setUserName("user").setPassword("test");
		int result = userMapper.insert(user);
		Assert.assertTrue("保存成功", result > 0);
		Assert.assertTrue("获取id成功，id=" + user.getId(), user.getId() != null);
	}

	@Test
	public void testInsertAll() {
		UserMapper userMapper = applicationContext.getBean(UserMapper.class);
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < 50; i++) {
			User user = new User().setUserName("user").setPassword("test" + i);
			users.add(user);
		}
		int result = userMapper.insertAll(users);
		Assert.assertTrue("保存成功", result > 0);
	}

	@Test
	public void testSelect() {
		UserMapper userMapper = applicationContext.getBean(UserMapper.class);
		User user = new User().setUserName("user").setPassword("test");
		userMapper.insert(user);
		List<User> users = userMapper.get(new Page(1, 2));
		Assert.assertTrue("查询成功", users != null && users.size() > 0);
	}

	@Test
	public void testSelectByUserNameAndLimit() {
		UserMapper userMapper = applicationContext.getBean(UserMapper.class);
		List<User> newUsers = new ArrayList<User>();
		for (int i = 0; i < 50; i++) {
			User user = new User().setUserName("user").setPassword("test" + i);
			newUsers.add(user);
		}
		userMapper.insertAll(newUsers);
		List<User> users = userMapper.getByUserName("user", new Limit(10));
		Assert.assertTrue("查询成功", users != null && users.size() > 0);
	}

	@Test
	public void testSelectByUserName() {
		UserMapper userMapper = applicationContext.getBean(UserMapper.class);
		User user = new User().setUserName("user").setPassword("test");
		userMapper.insert(user);
		List<User> users = userMapper.getByUserName("user", new Page(1, 10),new Sort []{new Sort("id",SortType.ASC)});
		Assert.assertTrue("查询成功", users != null && users.size() > 0);
	}

	@Test
	public void testDelete() {
		UserMapper userMapper = applicationContext.getBean(UserMapper.class);
		User user = new User().setUserName("user").setPassword("test");
		userMapper.insert(user);
		int result = userMapper.delete(user.getId());
		Assert.assertTrue("通过id删除成功", result > 0);
	}

	@Test
	public void testDeleteByUserName() {
		UserMapper userMapper = applicationContext.getBean(UserMapper.class);
		User user = new User().setUserName("user").setPassword("test");
		userMapper.insert(user);
		int result = userMapper.deleteByUserName("user");
		Assert.assertTrue("通过名称删除成功", result > 0);
	}

}
