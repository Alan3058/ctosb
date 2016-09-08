package com.ctosb.core.util;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

public class MybatisUtil {

	private static final String SPLIT = ",";

	/**
	 * copy a new mappedStatement instance
	 * 
	 * @param sql
	 * @param boundSql
	 * @param mappedStatement
	 * @return
	 * @author Alan
	 * @date 2016年6月30日 下午9:08:34
	 */
	public static MappedStatement createNewMappedStatement(String sql, BoundSql boundSql,
			MappedStatement mappedStatement) {
		BoundSql newBoundSql = createNewBoundSql(mappedStatement.getConfiguration(), sql, boundSql);
		MappedStatement newMappedStatement = createNewMappedStatement(mappedStatement, newBoundSql);
		return newMappedStatement;
	}

	/**
	 * copy a new mappedStatement instance
	 * 
	 * @param mappedStatement
	 * @param boundSql
	 * @return
	 * @author Alan
	 * @date 2016年6月30日 下午9:09:32
	 */
	private static MappedStatement createNewMappedStatement(MappedStatement mappedStatement, final BoundSql boundSql) {
		// build SqlSource instance
		SqlSource sqlSource = new SqlSource() {
			@Override
			public BoundSql getBoundSql(Object parameterObject) {
				return boundSql;
			}
		};
		// build MappedStatement.Builder instance and instantiation
		MappedStatement.Builder builder = new MappedStatement.Builder(mappedStatement.getConfiguration(),
				mappedStatement.getId(), sqlSource, mappedStatement.getSqlCommandType());
		builder.resource(mappedStatement.getResource());
		builder.fetchSize(mappedStatement.getFetchSize());
		builder.statementType(mappedStatement.getStatementType());
		builder.keyGenerator(mappedStatement.getKeyGenerator());
		builder.keyProperty(StringUtil.mergeStringArray(mappedStatement.getKeyProperties(), SPLIT));
		builder.keyColumn(StringUtil.mergeStringArray(mappedStatement.getKeyColumns(), SPLIT));
		builder.databaseId(mappedStatement.getDatabaseId());
		builder.lang(mappedStatement.getLang());
		builder.resultOrdered(mappedStatement.isResultOrdered());
		builder.resulSets(StringUtil.mergeStringArray(mappedStatement.getResulSets(), SPLIT));
		builder.resultMaps(mappedStatement.getResultMaps());
		builder.timeout(mappedStatement.getTimeout());
		builder.parameterMap(mappedStatement.getParameterMap());
		builder.resultMaps(mappedStatement.getResultMaps());
		builder.resultSetType(mappedStatement.getResultSetType());
		builder.cache(mappedStatement.getCache());
		builder.flushCacheRequired(mappedStatement.isFlushCacheRequired());
		builder.useCache(mappedStatement.isUseCache());

		// build MappedStatement instance
		return builder.build();
	}

	/**
	 * create a new boundSql instance
	 * 
	 * @param configuration
	 * @param sql
	 * @param boundSql
	 * @return
	 * @author Alan
	 * @date 2016年6月30日 下午9:09:46
	 */
	public static BoundSql createNewBoundSql(Configuration configuration, String sql, BoundSql boundSql) {
		return new BoundSql(configuration, sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
	}
}
