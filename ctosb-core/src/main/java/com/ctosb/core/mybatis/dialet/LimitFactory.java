package com.ctosb.core.mybatis.dialet;

import java.util.HashMap;
import java.util.Map;

/**
 * limit object factory
 * 
 * @author Alan
 *
 */
public class LimitFactory {

    private static Map<String, Limit> limitMap = new HashMap<String, Limit>();

    /**
     * get Limit class object based database type
     * 
     * @param dbType
     * @return
     * @author Alan
     * @createTime 2015年12月11日 下午2:58:30
     */
    public static Limit getLimit(String dbType) {
        // default database type is mysql
        dbType = (dbType == null ? "mysql" : dbType);
        return limitMap.get(dbType);
    }

    /**
     * register Limit
     * 
     * @param dbType
     * @param limit
     * @author Alan
     * @createTime 2015年12月11日 下午2:58:07
     */
    public static void registerLimit(String dbType, Limit limit) {
        limitMap.put(dbType, limit);
    }

    static {
        limitMap.put("mysql", new MysqlLimit());
        limitMap.put("sqlserver", new SqlserverLimit());
        limitMap.put("oracle", new OracleLimit());
    }


}
