package com.lagou.sqlSession;

import java.util.List;

/**
 * @ClassName: SqlSession
 * @Description:
 * @author: xsq
 * @date: 2020年03月28日
 */
public interface SqlSession {
    //查询所有
    <T> List<T> selectList(String statementId, Object... params);

    //根据条件查询单个
    <T> T selectOne(String statementId, Object... params);

    //为Dao接口生成代理实现类
    <T> T getMapper(Class<?> mapperClass);

    //增加
    int insert(String statementId, Object... params);

    //修改
    int update(String statementId, Object... params);

    //删除
    int delete(String statementId, Object... params);

}
