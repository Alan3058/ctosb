package com.ctosb.core.mybatis.dialet;

public class MysqlDialet extends AbstractDialet implements Dialet {

	@Override
	public String getLimitSql(String sql, int offset, int limit) {
		// build paging sql
		StringBuffer stringBuffer = new StringBuffer(sql);
		stringBuffer.append(" LIMIT ").append(offset).append(",").append(limit);
		return stringBuffer.toString();
	}

}
