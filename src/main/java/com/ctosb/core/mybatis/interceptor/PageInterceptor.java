package com.ctosb.core.mybatis.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.ctosb.core.mybatis.page.Limit;
import com.ctosb.core.mybatis.page.Page;
import com.ctosb.core.mybatis.page.PageList;
import com.ctosb.core.util.MybatisUtil;
import com.ctosb.core.util.ProcessUtil;

@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
		RowBounds.class, ResultHandler.class, }) })
public class PageInterceptor implements Interceptor {

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object intercept(Invocation invocation) throws Throwable {
		Executor executor = (Executor) invocation.getTarget();
		// get MappedStatement instance param
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		// get parameterObject param
		Object parameterObject = invocation.getArgs()[1];
		// get mybatis configuration object
		Configuration configuration = mappedStatement.getConfiguration();
		// get paging or limit infomation
		Object pageOrLimit = ProcessUtil.getPageOrLimit(parameterObject);
		Collection sorts = (Collection) ProcessUtil.getSort(parameterObject);
		parameterObject = ProcessUtil.extractParameterObject(parameterObject);
		invocation.getArgs()[1] = parameterObject;
		// get boundsql object
		BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
		if (pageOrLimit == null) {
			if (sorts != null && sorts.size() > 0) {
				String sql = ProcessUtil.getSortSql(boundSql.getSql(), configuration.getDatabaseId(), sorts);
				invocation.getArgs()[0] = MybatisUtil.createNewMappedStatement(sql, boundSql, mappedStatement);
			}
			return invocation.proceed();
		}

		String sql = boundSql.getSql();
		if (pageOrLimit instanceof Page) {
			Page page = (Page) pageOrLimit;
			// 1.execute count sql
			// convert countSql
			String countSql = ProcessUtil.getCountSql(sql, configuration.getDatabaseId());
			// execute count sql
			int count = executeCountSql(mappedStatement, executor.getTransaction().getConnection(), parameterObject,
					countSql);
			if (sorts != null && sorts.size() > 0) {
				sql = ProcessUtil.getSortSql(sql, configuration.getDatabaseId(), sorts);
			}
			// 2.execute limit sql
			// convert limit sql
			String limitSql = ProcessUtil.getLimitSql(sql, page, configuration.getDatabaseId());
			// copy a new MappedStatement instance
			invocation.getArgs()[0] = MybatisUtil.createNewMappedStatement(limitSql, boundSql, mappedStatement);
			// excute limit sql
			Object result = invocation.proceed();
			// convert result to PageList instance
			PageList<?> pageList = new PageList((Collection) result);
			pageList.setPage(page);
			page.setTotalRecord(count);
			// return PageList instance
			return pageList;
		} else if (pageOrLimit instanceof Limit) {
			Limit limit = (Limit) pageOrLimit;
			// 1.execute limit sql
			// convert limit sql
			if (sorts != null && sorts.size() > 0) {
				sql = ProcessUtil.getSortSql(sql, configuration.getDatabaseId(), sorts);
			}
			String limitSql = ProcessUtil.getLimitSql(sql, limit, configuration.getDatabaseId());
			invocation.getArgs()[0] = MybatisUtil.createNewMappedStatement(limitSql, boundSql, mappedStatement);
		}
		return invocation.proceed();
	}

	/**
	 * when the number of paramter equal 4 , exclude page or limit parameter,
	 * extract only one paramter that have not param annotation。<br/>
	 * for this example get(User,Page); ==> User <br>
	 * get(@Param("user")User,Page); ==>don't anything
	 * 
	 * @param parameterObject
	 * @return
	 */
	

	/**
	 * execute count sql
	 * 
	 * @param mappedStatement
	 * @param connection
	 * @param parameterObject
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @author Alan
	 * @date 2016年6月30日 下午9:13:01
	 */
	private int executeCountSql(MappedStatement mappedStatement, Connection connection, Object parameterObject,
			String sql) throws SQLException {
		BoundSql boundSql = new BoundSql(mappedStatement.getConfiguration(), sql,
				mappedStatement.getBoundSql(parameterObject).getParameterMappings(), parameterObject);
		return this.executeCountSql(mappedStatement, connection, parameterObject, boundSql);
	}

	/**
	 * execute count sql
	 * 
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

	@Override
	public Object plugin(Object target) {
		// if target object was intercepted,then return dynamic proxy object.or
		// else return the target objectr.
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	@Override
	public void setProperties(Properties properties) {
		// used specigy property of the outer properties config file
	}

}
