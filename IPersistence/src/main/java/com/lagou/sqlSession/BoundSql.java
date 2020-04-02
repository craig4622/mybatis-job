package com.lagou.sqlSession;

import com.lagou.utils.ParameterMapping;

import java.util.List;

/**
 * @ClassName: BoundSql
 * @Description:
 * @author: xsq
 * @date: 2020年03月28日
 */
public class BoundSql {

    private String sql;

    List<ParameterMapping> parameterMappings;


    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }
}
