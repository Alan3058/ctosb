package com.ctosb.core.mybatis.dialet;


public interface Limit {

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
}
