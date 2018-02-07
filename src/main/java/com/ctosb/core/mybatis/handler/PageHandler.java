
package com.ctosb.core.mybatis.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctosb.core.mybatis.page.Page;
import com.ctosb.core.mybatis.page.PageList;
import com.ctosb.core.util.MybatisUtil;
import com.ctosb.core.util.ProcessUtil;

/**
 * Page handler
 * @author Alan
 * @date 2018/2/7 15:52
 * @see
 */
public class PageHandler implements Handler {

	private final static Logger logger = LoggerFactory.getLogger(PageHandler.class);

	@Override
	public Object process(Invocation invocation, MappedStatement mappedStatement, Object pageOrLimit) throws Throwable {
		Page page = (Page) pageOrLimit;
		// get mybatis configuration object
		Configuration configuration = mappedStatement.getConfiguration();
		// get parameterObject param
		Object parameterObject = invocation.getArgs()[1];
		Collection sorts = (Collection) ProcessUtil.getSort(parameterObject);
		logger.debug("Sort parameter:{}.", sorts);
		parameterObject = ProcessUtil.extractParameterObject(parameterObject);
		logger.debug("Initial parameter:{}.", parameterObject);
		// rewrite parameter object, will exclude page or limit or sort parameter.
		invocation.getArgs()[1] = parameterObject;
		// get boundsql object
		BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
		String sql = boundSql.getSql();
		// 1.execute count sql
		int count = executeCountSql(invocation, mappedStatement, parameterObject, sql);
		logger.debug("excute count query result is {}.", count);
		if (count > 0) {
			if (sorts != null && sorts.size() > 0) {
				sql = ProcessUtil.getSortSql(sql, configuration.getDatabaseId(), sorts);
			}
			// 2.execute page sql
			// convert page sql
			String limitSql = ProcessUtil.getLimitSql(sql, page, configuration.getDatabaseId());
			logger.debug("excute sql:{}.", limitSql);
			// copy a new MappedStatement instance
			invocation.getArgs()[0] = MybatisUtil.createNewMappedStatement(limitSql, boundSql, mappedStatement);
			// excute page sql
			// convert result to PageList instance
			return new PageList((Collection) invocation.proceed()).setPage(page.setTotalPage(count));
		} else {
			logger.debug("excute count query result is {}, not need paging query.", count);
			// count value is zero, then not need paging query and return empty list
			return new PageList(new ArrayList()).setPage(page.setTotalPage(count));
		}
	}

	/**
	 * execute count sql
	 * @param invocation
	 * @param mappedStatement
	 * @param parameterObject
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @author Alan
	 * @date 2016年6月30日 下午9:13:01
	 */
	private int executeCountSql(Invocation invocation, MappedStatement mappedStatement, Object parameterObject,
			String sql) throws SQLException {
		Executor executor = (Executor) invocation.getTarget();
		// 1.execute count sql
		// convert countSql
		String countSql = ProcessUtil.getCountSql(sql, mappedStatement.getConfiguration().getDatabaseId());
		// execute count sql
		logger.debug("excute count sql:{}.", countSql);
		BoundSql boundSql = new BoundSql(mappedStatement.getConfiguration(), sql,
				mappedStatement.getBoundSql(parameterObject).getParameterMappings(), parameterObject);
		return this.executeCountSql(mappedStatement, executor.getTransaction().getConnection(), parameterObject,
				boundSql);
	}

	/**
	 * execute count sql
	 * @param mappedStatement
	 * @param connection
	 * @param parameterObject
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 * @author Alan
	 * @date 2016年6月30日 下午9:13:37
	 */
	private int executeCountSql(MappedStatement mappedStatement, Connection connection, Object parameterObject,
			BoundSql boundSql) throws SQLException {
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
		PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
		parameterHandler.setParameters(preparedStatement);
		ResultSet resultSet = preparedStatement.executeQuery();
		int count = 0;
		if (resultSet.next()) {
			count = resultSet.getInt(1);
		}
		return count;
	}
}
