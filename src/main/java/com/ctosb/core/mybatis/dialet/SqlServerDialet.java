
package com.ctosb.core.mybatis.dialet;

/**
 * SqlServerDialet
 * @date 2016年6月30日 下午9:11:14
 * @author alan
 * @since 1.0.0
 */
public class SqlServerDialet extends AbstractDialet implements Dialet {

	@Override
	public String getLimitSql(String sql, int offset, int limit) {
		// build paging sqlserver sql
		StringBuffer stringBuffer = new StringBuffer(
				"SELECT * FROM (SELECT TOP " + limit + " * FROM ( SELECT TOP " + (offset + limit) + " * FROM (");
		stringBuffer.append(sql).append(") ORDER BY ID ASC ) ORDER BY ID DESC) ORDER BY ID ASC");
		return stringBuffer.toString();
	}
}
