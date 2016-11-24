package com.ctosb.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ctosb.core.mapper.UserMapper;
import com.ctosb.core.model.User;

public class MybatisTest {

	SqlSessionFactory sessionFactory;

	@Before
	public void init() {
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream("mybatis-config.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	}

	@Test
	public void testInsert() {
		SqlSession sqlSession = sessionFactory.openSession();
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User user = new User().setAge(28).setUserName("user").setPassword("test");
		int result = userMapper.insert(user);
		sqlSession.commit();
		Assert.assertTrue("获取id成功，id=" + user.getId(), user.getId() != null);
		Assert.assertTrue("保存成功", result > 0);
	}

	@Test
	public void testSelect() {
		SqlSession sqlSession = sessionFactory.openSession();
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User user = new User().setAge(28).setUserName("user").setPassword("test");
		userMapper.insert(user);
		List<User> users = userMapper.getByUserName("user");
		sqlSession.commit();
		Assert.assertTrue("查询成功", users != null && users.size() > 0);

	}

	@Test
	public void testDelete() {
		SqlSession sqlSession = sessionFactory.openSession();
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User user = new User().setAge(28).setUserName("user").setPassword("test");
		userMapper.insert(user);
		int result = userMapper.delete(user.getId());
		sqlSession.commit();
		Assert.assertTrue("通过id删除成功", result >= 0);
	}

	@Test
	public void testDeleteByUserName() {
		SqlSession sqlSession = sessionFactory.openSession();
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User user = new User().setAge(28).setUserName("user").setPassword("test");
		userMapper.insert(user);
		int result = userMapper.deleteByUserName("user");
		sqlSession.commit();
		Assert.assertTrue("通过名称删除成功", result > 0);
	}

}
