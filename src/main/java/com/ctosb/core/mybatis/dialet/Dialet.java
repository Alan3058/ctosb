package com.ctosb.core.mybatis.dialet;

import java.util.Collection;

import com.ctosb.core.mybatis.sort.Sort;

public interface Dialet {

	/**
	 * get limiting sql
	 * 
	 * @param sql
	 * @param offset
	 * @param limit
	 * @return
	 * @author Alan
	 * @date 2015年12月10日 下午11:25:19
	 */
	String getLimitSql(String sql, int offset, int limit);

	/**
	 * get count sql
	 * 
	 * @param sql
	 * @return
	 * @author Alan
	 * @date 2016年6月30日 下午2:37:22
	 */
	String getCountSql(String sql);
	
	/**
	 * get count sql
	 * 
	 * @param sql
	 * @param sorts
	 * @return
	 * @author Alan
	 * @date 2016年6月30日 下午2:37:22
	 */
	String getSortSql(String sql,Collection<Sort> sorts);
}
