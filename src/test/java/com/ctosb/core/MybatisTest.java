
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

	/**
	 * 测试插入
	 * @date 2018/2/6 13:21
	 * @author Alan
	 * @since 1.0.0
	 */
	@Test
	public void testInsert() {
		SqlSession sqlSession = sessionFactory.openSession();
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User user = new User().setUserName("user").setPassword("test");
		int result = userMapper.insert(user);
		sqlSession.commit();
		Assert.assertTrue(user.getId() != null);
		Assert.assertTrue(result > 0);
	}

	/**
	 * 测试查询
	 * @date 2018/2/6 13:22
	 * @author Alan
	 * @since 1.0.0
	 */
	@Test
	public void testSelect() {
		SqlSession sqlSession = sessionFactory.openSession();
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User user = new User().setUserName("user").setPassword("test");
		userMapper.insert(user);
		List<User> users = userMapper.getByUserName("user");
		sqlSession.commit();
		Assert.assertTrue(users != null && users.size() > 0);
	}

	/**
	 * 测试通过id删除
	 * @date 2018/2/6 13:26
	 * @author Alan
	 * @since 1.0.0
	 */
	@Test
	public void testDelete() {
		SqlSession sqlSession = sessionFactory.openSession();
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User user = new User().setUserName("user").setPassword("test");
		userMapper.insert(user);
		int result = userMapper.delete(user.getId());
		sqlSession.commit();
		Assert.assertTrue(result >= 0);
	}

	/**
	 * 测试按用户名删除
	 * @date 2018/2/6 13:27
	 * @author Alan
	 * @since 1.0.0
	 */
	@Test
	public void testDeleteByUserName() {
		SqlSession sqlSession = sessionFactory.openSession();
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User user = new User().setUserName("user").setPassword("test");
		userMapper.insert(user);
		int result = userMapper.deleteByUserName("user");
		sqlSession.commit();
		Assert.assertTrue(result > 0);
	}
}
