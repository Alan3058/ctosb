
package com.ctosb.core.mybatis.handler;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;

/**
 * interceptor handler interface
 * @author Alan
 * @date 2018/2/7 15:47
 * @see
 */
public interface Handler {

	Object process(Invocation invocation, MappedStatement mappedStatement, Object pageOrLimit) throws Throwable;
}
