package com.ctosb.core.mybatis.dialet;

public abstract class AbstractDialet implements Dialet {

	@Override
	public String getCountSql(String sql) {
		return "SELECT COUNT(1) FROM (" + sql + ") TMP";
	}

}
