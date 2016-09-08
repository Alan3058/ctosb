package com.ctosb.core.mybatis.interceptor;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;

import com.ctosb.core.mybatis.Page;
import com.ctosb.core.util.PageUtil;
import com.ctosb.core.util.ReflectUtil;

/**
 * please use PageInterceptor, it's contain full function
 *
 * @author Alan
 * @date 2016年6月30日 下午9:23:32
 */
@Deprecated
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class LimitInterceptor implements Interceptor {

	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler handler = (StatementHandler) invocation.getTarget();
		// get metaObject source object
		MetaObject metaStatementHandler = MetaObject.forObject(handler, DEFAULT_OBJECT_FACTORY,
				DEFAULT_OBJECT_WRAPPER_FACTORY);
		// get mybatis configuration object
		Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
		// get boundsql object
		BoundSql boundSql = handler.getBoundSql();
		// get paging infomation
		Page page = PageUtil.getPage(boundSql.getParameterObject());
		// join paging sql
		String sql = PageUtil.getLimitSql(boundSql.getSql(), page, configuration.getDatabaseId());
		// write back set sql to boundsql object
		ReflectUtil.setFieldValue(boundSql, "sql", sql);
		return invocation.proceed();
	}

	public Object plugin(Object target) {
		// if target object was intercepted,then return dynamic proxy object.or
		// else return the target objectr.
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	public void setProperties(Properties properties) {
		// used specigy property of the outer properties config file
	}

}
