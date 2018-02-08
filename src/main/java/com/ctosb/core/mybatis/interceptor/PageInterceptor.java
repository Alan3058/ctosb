
package com.ctosb.core.mybatis.interceptor;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.ctosb.core.mybatis.handler.LimitHandler;
import com.ctosb.core.mybatis.handler.NotLimitPageHandler;
import com.ctosb.core.mybatis.handler.PageHandler;
import com.ctosb.core.mybatis.page.Limit;
import com.ctosb.core.mybatis.page.Page;
import com.ctosb.core.util.ProcessUtil;

/**
 * PageInterceptor
 * @date 2016年6月30日 下午9:11:14
 * @author alan
 * @since 1.0.0
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
		RowBounds.class, ResultHandler.class }) })
public class PageInterceptor implements Interceptor {

	private final static Log logger = LogFactory.getLog(PageInterceptor.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// get MappedStatement instance param
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		// get parameterObject param
		Object parameterObject = invocation.getArgs()[1];
		// extract paging or limit parameter
		Object pageOrLimit = ProcessUtil.getPageOrLimit(parameterObject);
		invocation.getArgs()[1] = parameterObject;
		if (pageOrLimit == null) {
			// not need paging or limit
			logger.debug("there aren't paging or limit paremeter ,not need paging or limit query.");
			return new NotLimitPageHandler().process(invocation, mappedStatement, pageOrLimit);
		}
		if (pageOrLimit instanceof Page) {
			logger.debug("excute paging query.");
			return new PageHandler().process(invocation, mappedStatement, pageOrLimit);
		} else if (pageOrLimit instanceof Limit) {
			logger.debug("excute limit query.");
			return new LimitHandler().process(invocation, mappedStatement, pageOrLimit);
		}
		return invocation.proceed();
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
