package com.lagou.sqlSession;

import com.lagou.config.XmlConfigBuilder;
import com.lagou.pojo.Configuration;

import java.io.InputStream;

/**
 * @ClassName: SqlSessionFactory
 * @Description:
 * @author: xsq
 * @date: 2020年03月28日
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream in) {
        //使用dom4j解析配置文件并封装到Configuration类中
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfig(in);
        //创建SqlSessionFactory对象
        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return defaultSqlSessionFactory;
    }
}
