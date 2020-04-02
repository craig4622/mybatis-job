package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;

import java.lang.reflect.*;
import java.util.List;

/**
 * @ClassName: DefaultSqlSession
 * @Description:
 * @author: xsq
 * @date: 2020年03月28日
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }


    public List selectList(String statementId, Object... params) {
        //将要去完成对SimpleExecutor里的query的方法的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatement().get(statementId);
        List<Object> objects = simpleExecutor.query(configuration, mappedStatement, params);
        return objects;
    }


    public Object selectOne(String statementId, Object... params) {
        List<Object> obejects = selectList(statementId, params);
        if (obejects != null && obejects.size() == 1) {
            return obejects.get(0);
        } else {
            throw new RuntimeException("查询结果为空或者结果过多");
        }
    }

    public int insert(String statementId, Object... params) {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatement().get(statementId);
        int row = simpleExecutor.update(configuration, mappedStatement, params);
        return row;
    }

    public int update(String statementId, Object... params) {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatement().get(statementId);
        int row = simpleExecutor.update(configuration, mappedStatement, params);
        return row;
    }

    public int delete(String statementId, Object... params) {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatement().get(statementId);
        int row = simpleExecutor.update(configuration, mappedStatement, params);
        return row;
    }


    /**
     * 为Dao接口生成代理实现类
     *
     * @return
     */

    public <T> T getMapper(Class<?> mapperClass) {
        //使用JDK动态代理为Dao接口生成代理对象,并返回
        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //proxy:当前代理对象的引用,method:当前被调用方法的引用,args:传递的参数
                //底层还是要调用JDBC的代码 //根据不同情况调用selectList 或者selectOne
                //准备参数:statementId(sql语句唯一标识)

                //方法名
                String methodName = method.getName();
                //方法所在类的名称
                String className = method.getDeclaringClass().getName();
                String statementId = className + "." + methodName;
                MappedStatement mappedStatement = configuration.getMappedStatement().get(statementId);
                Integer sqlCommandType = mappedStatement.getSqlCommandType();
                //准备参数2:params:args

                if (sqlCommandType == 0) {
                    //查询
                    //获取被调用方法的返回值类型
                    Type genericReturnType = method.getGenericReturnType();
                    //判断是否进行了泛型化参数
                    if (genericReturnType instanceof ParameterizedType) {
                        List list = selectList(statementId, args);
                        return list;
                    } else {
                        return selectOne(statementId, args);
                    }
                } else if (sqlCommandType == 1) {
                    //添加
                    int row = insert(statementId, args);
                    return row;
                } else if (sqlCommandType == 2) {
                    //修改
                    int row = update(statementId, args);
                    return row;
                } else if (sqlCommandType == 3) {
                    //删除
                    int row = delete(statementId, args);
                    return row;
                }
                return selectOne(statementId, args);
            }
        });
        return (T) proxyInstance;
    }


}
