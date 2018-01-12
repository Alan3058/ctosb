
package com.ctosb.core.mybatis.dialet;

/**
 * OracleDialet
 * @date 2016年6月30日 下午9:11:14
 * @author alan
 * @since 1.0.0
 */
public class OracleDialet extends AbstractDialet implements Dialet {

	@Override
	public String getLimitSql(String sql, int offset, int limit) {
		// build paging oracle sql
		StringBuffer stringBuffer = new StringBuffer("SELECT * FROM (SELECT ROWNUM AS _NUM,_TMP.* FROM ( ");
		stringBuffer.append(sql);
		stringBuffer.append(" ) _TMP ) WHERE _NUM > ").append(offset).append(" AND _NUM <= ").append(offset + limit);
		return stringBuffer.toString();
	}
}
