package com.ctosb.core.mybatis;

import java.io.IOException;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import com.ctosb.core.mybatis.interceptor.PageInterceptor;

public class SqlSessionFactoryBean extends org.mybatis.spring.SqlSessionFactoryBean {

	@Override
	protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
		SqlSessionFactory sqlSessionFactory = super.buildSqlSessionFactory();
		// get configuaration object(mybatis config infomation)
		Configuration configuration = sqlSessionFactory.getConfiguration();
		// set database field camel case resolver
		configuration.setMapUnderscoreToCamelCase(true);
		// set use auto generated key
		configuration.setUseGeneratedKeys(true);
		// add the paging interceptor
		configuration.addInterceptor(new PageInterceptor());
		return sqlSessionFactory;
	}

}
