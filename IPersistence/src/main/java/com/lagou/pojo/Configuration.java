package com.lagou.pojo;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: Configuration
 * @Description:
 * @author: xsq
 * @date: 2020年03月28日
 */
public class Configuration {

    private DataSource dataSource;
    //key:sql唯一标识:由namespace.id来组成: statementId,vlaue:封装好的MappedStatement对象
    Map<String, MappedStatement> mappedStatement = new HashMap<String, MappedStatement>();

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MappedStatement> getMappedStatement() {
        return mappedStatement;
    }

    public void setMappedStatement(Map<String, MappedStatement> mappedStatement) {
        this.mappedStatement = mappedStatement;
    }
}
