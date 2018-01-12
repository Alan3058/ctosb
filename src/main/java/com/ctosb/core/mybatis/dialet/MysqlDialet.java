
package com.ctosb.core.mybatis.dialet;

/**
 * MysqlDialet
 * @date 2016年6月30日 下午9:11:14
 * @author alan
 * @since 1.0.0
 */
public class MysqlDialet extends AbstractDialet implements Dialet {

	@Override
	public String getLimitSql(String sql, int offset, int limit) {
		// build paging sql
		StringBuffer stringBuffer = new StringBuffer(sql);
		stringBuffer.append(" LIMIT ").append(offset).append(",").append(limit);
		return stringBuffer.toString();
	}
}
