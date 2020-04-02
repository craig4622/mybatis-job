package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;

/**
 * @ClassName: Executor
 * @Description:
 * @author: xsq
 * @date: 2020年03月28日
 */
public interface Executor {
    <T> T query(Configuration configuration, MappedStatement mappedStatement, Object... params);
}
