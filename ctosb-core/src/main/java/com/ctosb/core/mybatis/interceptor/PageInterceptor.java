package com.ctosb.core.mybatis.interceptor;

import java.sql.Statement;
import java.util.Map;
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
import org.apache.ibatis.session.ResultHandler;

import com.ctosb.core.mybatis.Page;
import com.ctosb.core.mybatis.dialet.LimitFactory;
import com.ctosb.core.util.ReflectUtil;

@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})})
public class PageInterceptor implements Interceptor {

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler) invocation.getTarget();
        // get metaObject source object
        MetaObject metaStatementHandler = MetaObject.forObject(handler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
        // get mybatis configuration object
        Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
        // get boundsql object
        BoundSql boundSql = handler.getBoundSql();
        // get paging infomation
        Page page = getPage(boundSql.getParameterObject());
        // join paging sql
        String sql = getLimitSql(boundSql.getSql(), page, configuration.getDatabaseId());
        // write back set sql to boundsql object
        ReflectUtil.setFieldValue(boundSql, "sql", sql);
        return invocation.proceed();
    }

    public Object plugin(Object target) {
        // if target object was intercepted,then return dynamic proxy object.or else return the target objectr.
        return Plugin.wrap(target, this);
    }

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
        sql = LimitFactory.getLimit(dbType).getLimitSql(sql, offset, limit);
        return sql;
    }

}
