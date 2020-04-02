package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;

/**
 * @ClassName: DefaultSqlSessionFactory
 * @Description:
 * @author: xsq
 * @date: 2020年03月28日
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
