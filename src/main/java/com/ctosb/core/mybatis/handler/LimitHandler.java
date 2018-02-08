
package com.ctosb.core.mybatis.handler;

import java.util.Collection;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.Configuration;

import com.ctosb.core.mybatis.page.Limit;
import com.ctosb.core.util.MybatisUtil;
import com.ctosb.core.util.ProcessUtil;

/**
 * Limit handler
 * @author Alan
 * @date 2018/2/7 15:52
 * @see
 */
public class LimitHandler implements Handler {

	private final static Log logger = LogFactory.getLog(LimitHandler.class);

	@Override
	public Object process(Invocation invocation, MappedStatement mappedStatement, Object pageOrLimit) throws Throwable {
		Limit limit = (Limit) pageOrLimit;
		// get parameterObject param
		Object parameterObject = invocation.getArgs()[1];
		// get mybatis configuration object
		Configuration configuration = mappedStatement.getConfiguration();
		Collection sorts = (Collection) ProcessUtil.getSort(parameterObject);
		logger.debug(String.format("Sort parameter:%s.", sorts));
		parameterObject = ProcessUtil.extractParameterObject(parameterObject);
		logger.debug(String.format("Initial parameter:%s.", parameterObject));
		// rewrite parameter object, will exclude page or limit or sort parameter.
		invocation.getArgs()[1] = parameterObject;
		// get boundsql object
		BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
		String sql = boundSql.getSql();
		// 1.execute limit sql
		// convert limit sql
		if (sorts != null && sorts.size() > 0) {
			sql = ProcessUtil.getSortSql(sql, configuration.getDatabaseId(), sorts);
		}
		String limitSql = ProcessUtil.getLimitSql(sql, limit, configuration.getDatabaseId());
		logger.debug(String.format("excute sql:%s.", limitSql));
		invocation.getArgs()[0] = MybatisUtil.createNewMappedStatement(limitSql, boundSql, mappedStatement);
		return invocation.proceed();
	}
}
