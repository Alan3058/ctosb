package com.ctosb.core.mybatis.dialet;

import java.util.HashMap;
import java.util.Map;

/**
 * Dialet object factory
 * 
 * @author Alan
 *
 */
public class DialetFactory {

	private static Map<String, Dialet> DialetMap = new HashMap<String, Dialet>();

	/**
	 * get Dialet class object based database type
	 * 
	 * @param dbType
	 * @return
	 * @author Alan
	 * @createTime 2015年12月11日 下午2:58:30
	 */
	public static Dialet getLimit(String dbType) {
		// default database type is mysql
		dbType = (dbType == null ? "mysql" : dbType);
		return DialetMap.get(dbType);
	}

	/**
	 * register Dialet
	 * 
	 * @param dbType
	 * @param dialet
	 * @author Alan
	 * @createTime 2015年12月11日 下午2:58:07
	 */
	public static void registerLimit(String dbType, Dialet dialet) {
		DialetMap.put(dbType, dialet);
	}

	static {
		DialetMap.put("mysql", new MysqlDialet());
		DialetMap.put("sqlserver", new SqlServerDialet());
		DialetMap.put("oracle", new OracleDialet());
	}

}
