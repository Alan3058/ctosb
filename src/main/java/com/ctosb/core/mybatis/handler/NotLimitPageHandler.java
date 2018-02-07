
package com.ctosb.core.mybatis.handler;

import java.util.Collection;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctosb.core.util.MybatisUtil;
import com.ctosb.core.util.ProcessUtil;

/**
 * have not Limit and Page handler
 * @author Alan
 * @date 2018/2/7 15:52
 * @see
 */
public class NotLimitPageHandler implements Handler {

	private final static Logger logger = LoggerFactory.getLogger(NotLimitPageHandler.class);

	@Override
	public Object process(Invocation invocation, MappedStatement mappedStatement, Object pageOrLimit) throws Throwable {
		// get parameterObject param
		Object parameterObject = invocation.getArgs()[1];
		// get mybatis configuration object
		Configuration configuration = mappedStatement.getConfiguration();
		// extract sort parameter
		Collection sorts = (Collection) ProcessUtil.getSort(parameterObject);
		if (sorts != null && sorts.size() > 0) {
			// sort parameter exists.
			logger.debug("Sort parameter:{}.", sorts);
			// extract parameter object
			parameterObject = ProcessUtil.extractParameterObject(parameterObject);
			logger.debug("Initial parameter:{}.", parameterObject);
			// rewrite parameter object, will exclude page or limit or sort parameter.
			invocation.getArgs()[1] = parameterObject;
			// get boundsql object
			BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
			String sql = ProcessUtil.getSortSql(boundSql.getSql(), configuration.getDatabaseId(), sorts);
			logger.debug("excute sql:{}.", sql);
			invocation.getArgs()[0] = MybatisUtil.createNewMappedStatement(sql, boundSql, mappedStatement);
		}
		return invocation.proceed();
	}
}
