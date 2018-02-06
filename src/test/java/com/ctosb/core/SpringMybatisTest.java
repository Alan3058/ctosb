
package com.ctosb.core;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
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
	UserMapper userMapper;

	@BeforeClass
	public static void beforeClass() {
		// EmbeddedDatabase database = new
		// EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
		// .addScript("classpath:sql/schema.sql").build();
	}

	@Before
	public void init() throws Exception {
		String dataSql = "/sql/schema.sql";
		applicationContext = new ClassPathXmlApplicationContext("spring-mybatis.xml");
		userMapper = applicationContext.getBean(UserMapper.class);
		DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");
		Connection connection = dataSource.getConnection();
		Statement statement = connection.createStatement();
		statement.execute("RUNSCRIPT FROM  'classpath:" + dataSql + "'");
		statement.close();
		connection.close();
	}

	/**
	 * 测试插入
	 * @date 2018/2/6 13:21
	 * @author Alan
	 * @since 1.0.0
	 */
	@Test
	public void testInsert() {
		User user = new User().setUserName("user").setPassword("test");
		int result = userMapper.insert(user);
		Assert.assertTrue(result > 0);
	}

	/**
	 * 测试批量插入
	 * @date 2018/2/6 13:21
	 * @author Alan
	 * @since 1.0.0
	 */
	@Test
	public void testInsertAll() {
		UserMapper userMapper = applicationContext.getBean(UserMapper.class);
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < 50; i++) {
			User user = new User().setUserName("user").setPassword("test" + i);
			users.add(user);
		}
		int result = userMapper.insertAll(users);
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
		UserMapper userMapper = applicationContext.getBean(UserMapper.class);
		User user = new User().setUserName("user").setPassword("test");
		userMapper.insert(user);
		List<User> users = userMapper.get(new Page(1, 2));
		Assert.assertTrue(users != null && users.size() > 0);
	}

	/**
	 * 测试limit查询
	 * @date 2018/2/6 13:23
	 * @author Alan
	 * @since 1.0.0
	 */
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
		Assert.assertTrue(users != null && users.size() > 0);
	}

	/**
	 * 测试条件分页查询
	 * @date 2018/2/6 13:25
	 * @author Alan
	 * @since 1.0.0
	 */
	@Test
	public void testSelectByUserNameAndPage() {
		UserMapper userMapper = applicationContext.getBean(UserMapper.class);
		User user = new User().setUserName("user").setPassword("test");
		userMapper.insert(user);
		List<User> users = userMapper.getByUserName("user", new Page(1, 10),
				new Sort[] { new Sort("id", SortType.ASC) });
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
		UserMapper userMapper = applicationContext.getBean(UserMapper.class);
		User user = new User().setUserName("user").setPassword("test");
		userMapper.insert(user);
		int result = userMapper.delete(user.getId());
		Assert.assertTrue("通过id删除成功", result > 0);
	}

	/**
	 * 测试按用户名删除
	 * @date 2018/2/6 13:27
	 * @author Alan
	 * @since 1.0.0
	 */
	@Test
	public void testDeleteByUserName() {
		UserMapper userMapper = applicationContext.getBean(UserMapper.class);
		User user = new User().setUserName("user").setPassword("test");
		userMapper.insert(user);
		int result = userMapper.deleteByUserName("user");
		Assert.assertTrue("通过名称删除成功", result > 0);
	}
}
