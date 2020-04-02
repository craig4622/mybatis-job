package com.lagou.pojo;

/**
 * @ClassName: MappedStatement
 * @Description:
 * @author: xsq
 * @date: 2020年03月28日
 */
public class MappedStatement {
    //id
    private String id;
    //返回值类型
    private String resultType;
    //参数类型
    private String paramterType;
    //sql
    private String sql;
    //0:查询,1:增加,2:修改,3:删除
    private Integer sqlCommandType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getParamterType() {
        return paramterType;
    }

    public void setParamterType(String paramterType) {
        this.paramterType = paramterType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Integer getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(Integer sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }
}
