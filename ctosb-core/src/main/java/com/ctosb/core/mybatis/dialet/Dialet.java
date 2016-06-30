package com.ctosb.core.mybatis.dialet;

public interface Dialet {

	/**
	 * get limiting sql
	 * 
	 * @param sql
	 * @param offset
	 * @param limit
	 * @return
	 * @author Alan
	 * @createTime 2015年12月10日 下午11:25:19
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
}
