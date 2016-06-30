package com.ctosb.core.util;

import java.util.Map;

import com.ctosb.core.mybatis.Limit;
import com.ctosb.core.mybatis.Page;
import com.ctosb.core.mybatis.dialet.DialetFactory;

public class PageUtil {
	/**
	 * get page or limit instance from paramterObject
	 * 
	 * @param parameterObj
	 * @return
	 * @author Alan
	 * @date 2016年6月30日 下午9:11:14
	 */
	public static Object getPageOrLimit(Object parameterObj) {
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
	 * get paging infomation
	 * 
	 * @param parameterObj
	 * @return
	 * @author Alan
	 * @createTime 2015年12月12日 上午8:38:59
	 */
	public static Page getPage(Object parameterObj) {
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
	 * get count sql
	 * 
	 * @param sql
	 * @param page
	 * @param dbType
	 * @return
	 * @author Alan
	 * @createTime 2015年12月12日 下午1:22:05
	 */
	public static String getCountSql(String sql, String dbType) {
		return DialetFactory.getLimit(dbType).getCountSql(sql);
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
	public static String getLimitSql(String sql, Page page, String dbType) {
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
	public static String getLimitSql(String sql, Limit limit, String dbType) {
		// if the paging object is null，return source sql
		if (limit == null) {
			return sql;
		}
		// join limit sql
		sql = DialetFactory.getLimit(dbType).getLimitSql(sql, limit.getOffset(), limit.getCount());
		return sql;
	}
}
