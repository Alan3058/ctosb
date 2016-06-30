package com.ctosb.core.mybatis.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.ctosb.core.mybatis.Limit;
import com.ctosb.core.mybatis.Page;
import com.ctosb.core.mybatis.PageList;
import com.ctosb.core.mybatis.dialet.DialetFactory;

@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
		RowBounds.class, ResultHandler.class, }) })
public class PageInterceptor implements Interceptor {

	private static final String SPLIT = ",";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Executor executor = (Executor) invocation.getTarget();
		// get MappedStatement instance param
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		// get parameterObject param
		Object parameterObject = invocation.getArgs()[1];
		// get mybatis configuration object
		Configuration configuration = mappedStatement.getConfiguration();
		// get boundsql object
		BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
		// get paging or limit infomation
		Object pageOrLimit = getPageOrLimit(boundSql.getParameterObject());
		if (pageOrLimit == null) {
			return invocation.proceed();
		}

		if (pageOrLimit instanceof Page) {
			Page page = (Page) pageOrLimit;
			// 1.execute count sql
			// convert countSql
			String countSql = DialetFactory.getLimit(configuration.getDatabaseId()).getCountSql(boundSql.getSql());
			// execute count sql
			int count = executeCountSql(mappedStatement, executor.getTransaction().getConnection(), parameterObject,
					countSql);
			// 2.execute limit sql
			// convert limit sql
			String limitSql = getLimitSql(boundSql.getSql(), page, configuration.getDatabaseId());
			// copy a new MappedStatement instance
			invocation.getArgs()[0] = createNewMappedStatement(limitSql, boundSql, mappedStatement);
			// excute limit sql
			Object result = invocation.proceed();
			PageList pageList = new PageList((Collection) result);
			pageList.setPage(page);
			page.setTotalRecord(count);
			return pageList;
		} else if (pageOrLimit instanceof Limit) {
			// 1.execute limit sql
			Limit limit = (Limit) pageOrLimit;
			// join paging sql
			String limitSql = getLimitSql(boundSql.getSql(), limit, configuration.getDatabaseId());
			invocation.getArgs()[0] = createNewMappedStatement(limitSql, boundSql, mappedStatement);
		}
		return invocation.proceed();
	}

	/**
	 * copy a new mappedStatement instance
	 * 
	 * @param sql
	 * @param boundSql
	 * @param mappedStatement
	 * @return
	 * @author Alan
	 * @date 2016年6月30日 下午9:08:34
	 */
	private MappedStatement createNewMappedStatement(String sql, BoundSql boundSql, MappedStatement mappedStatement) {
		BoundSql newBoundSql = createNewBoundSql(mappedStatement.getConfiguration(), sql, boundSql);
		MappedStatement newMappedStatement = createNewMappedStatement(mappedStatement, newBoundSql);
		return newMappedStatement;
	}

	/**
	 * copy a new mappedStatement instance
	 * 
	 * @param mappedStatement
	 * @param boundSql
	 * @return
	 * @author Alan
	 * @date 2016年6月30日 下午9:09:32
	 */
	private MappedStatement createNewMappedStatement(MappedStatement mappedStatement, final BoundSql boundSql) {
		// build SqlSource instance
		SqlSource sqlSource = new SqlSource() {
			@Override
			public BoundSql getBoundSql(Object parameterObject) {
				return boundSql;
			}
		};
		// build MappedStatement.Builder instance and instantiation
		MappedStatement.Builder builder = new MappedStatement.Builder(mappedStatement.getConfiguration(),
				mappedStatement.getId(), sqlSource, mappedStatement.getSqlCommandType());
		builder.resource(mappedStatement.getResource());
		builder.fetchSize(mappedStatement.getFetchSize());
		builder.statementType(mappedStatement.getStatementType());
		builder.keyGenerator(mappedStatement.getKeyGenerator());
		builder.keyProperty(mergeStringArray(mappedStatement.getKeyProperties(), SPLIT));
		builder.keyColumn(mergeStringArray(mappedStatement.getKeyColumns(), SPLIT));
		builder.databaseId(mappedStatement.getDatabaseId());
		builder.lang(mappedStatement.getLang());
		builder.resultOrdered(mappedStatement.isResultOrdered());
		builder.resulSets(mergeStringArray(mappedStatement.getResulSets(), SPLIT));
		builder.resultMaps(mappedStatement.getResultMaps());
		builder.timeout(mappedStatement.getTimeout());
		builder.parameterMap(mappedStatement.getParameterMap());
		builder.resultMaps(mappedStatement.getResultMaps());
		builder.resultSetType(mappedStatement.getResultSetType());
		builder.cache(mappedStatement.getCache());
		builder.flushCacheRequired(mappedStatement.isFlushCacheRequired());
		builder.useCache(mappedStatement.isUseCache());

		// build MappedStatement instance
		return builder.build();
	}

	/**
	 * create a new boundSql instance
	 * 
	 * @param configuration
	 * @param sql
	 * @param boundSql
	 * @return
	 * @author Alan
	 * @date 2016年6月30日 下午9:09:46
	 */
	private BoundSql createNewBoundSql(Configuration configuration, String sql, BoundSql boundSql) {
		return new BoundSql(configuration, sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
	}

	/**
	 * get page or limit instance from paramterObject
	 * 
	 * @param parameterObj
	 * @return
	 * @author Alan
	 * @date 2016年6月30日 下午9:11:14
	 */
	private Object getPageOrLimit(Object parameterObj) {
		Object object = null;
		// extract the page object from the input param
		if (Map.class.isInstance(parameterObj)) {
			for (Object obj : ((Map<?, ?>) parameterObj).values()) {
				if (Page.class.isInstance(obj) || Limit.class.isInstance(obj)) {
					object = obj;
					break;
				}
			}
		} else if (Page.class.isInstance(parameterObj) || Limit.class.isInstance(parameterObj)) {
			object = parameterObj;
		}
		return object;
	}

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

	/**
	 * merge string array
	 * 
	 * @param strings
	 * @param split
	 * @return
	 * @author Alan
	 * @date 2016年6月30日 下午9:13:51
	 */
	private String mergeStringArray(String[] strings, String split) {
		if (strings == null) {
			return "";
		}
		StringBuffer stringBuffer = new StringBuffer();
		for (String string : strings) {
			stringBuffer.append(split + string);
		}
		return stringBuffer.substring(1);
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

	/**
	 * get paging infomation
	 * 
	 * @param parameterObj
	 * @return
	 * @author Alan
	 * @createTime 2015年12月12日 上午8:38:59
	 */
	private Page getPage(Object parameterObj) {
		Page page = null;
		// extract the page object from the input param
		if (Map.class.isInstance(parameterObj)) {
			for (Object obj : ((Map<?, ?>) parameterObj).values()) {
				if (Page.class.isInstance(obj)) {
					page = (Page) obj;
					break;
				}
			}
		} else if (Page.class.isInstance(parameterObj)) {
			page = (Page) parameterObj;
		}
		return page;
	}

	/**
	 * get limit sql
	 * 
	 * @param sql
	 * @param page
	 * @param dbType
	 * @return
	 * @author Alan
	 * @createTime 2015年12月12日 下午1:22:05
	 */
	private String getLimitSql(String sql, Page page, String dbType) {
		// if the paging object is null，return source sql
		if (page == null) {
			return sql;
		}
		// join limit sql
		int offset = (page.getPageNum() - 1) * page.getPageSize();
		int limit = page.getPageSize();
		sql = DialetFactory.getLimit(dbType).getLimitSql(sql, offset, limit);
		return sql;
	}

	/**
	 * get limit sql
	 * 
	 * @param sql
	 * @param limit
	 * @param dbType
	 * @return
	 * @author Alan
	 * @date 2016年6月30日 下午2:17:18
	 */
	private String getLimitSql(String sql, Limit limit, String dbType) {
		// if the paging object is null，return source sql
		if (limit == null) {
			return sql;
		}
		// join limit sql
		sql = DialetFactory.getLimit(dbType).getLimitSql(sql, limit.getOffset(), limit.getCount());
		return sql;
	}

}
