package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import com.lagou.utils.GenericTokenParser;
import com.lagou.utils.ParameterMapping;
import com.lagou.utils.ParameterMappingTokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: SimpleExecutor
 * @Description:
 * @author: xsq
 * @date: 2020年03月28日
 */
public class SimpleExecutor implements Executor {

    public <T> T query(Configuration configuration, MappedStatement mappedStatement, Object... params) {
        ArrayList<Object> objects = null;
        try {
            //1.注册驱动,获取连接
            Connection connection = configuration.getDataSource().getConnection();
            //2.获取sql语句: select * from user where id = #{id} and username = #{username}
            String sql = mappedStatement.getSql();
            //转换sql语句.jdbc不能识别#{id}这种占位符,只会识别select * from user where id = ? and username = ?,
            //我们需要对 #{}进行解析存储
            BoundSql boundSql = getBoundSql(sql);
            //3.获取预处理对象
            PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
            //4.设置参数
            //获取入参的全路径通过反射得到Class对象
            String paramterType = mappedStatement.getParamterType();
            Class<?> classType = getClassType(paramterType);
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            for (int i = 0; i < parameterMappings.size(); i++) {
                String content = parameterMappings.get(i).getContent();
                //通过反射获取实体对象中的值
                Field declaredField = classType.getDeclaredField(content);
                //暴力访问
                declaredField.setAccessible(true);
                Object o = declaredField.get(params[0]);
                preparedStatement.setObject(i + 1, o);
            }
            //5.执行sql
            ResultSet resultSet = preparedStatement.executeQuery();
            //获取结果集
            //获取返回参数的全路径通过反射得到Class对象
            String resultType = mappedStatement.getResultType();
            Class<?> resultTypeClass = getClassType(resultType);
            objects = new ArrayList<Object>();
            //6.封装结果集
            while (resultSet.next()) {
                Object o = resultTypeClass.newInstance();
                //元数据(它包含了字段名)
                ResultSetMetaData metaData = resultSet.getMetaData();
                for (int i = 1; i < metaData.getColumnCount() + 1; i++) {
                    //字段名
                    String columnName = metaData.getColumnName(i);
                    //字段值
                    Object value = resultSet.getObject(columnName);
                    //利用反射或者内省,根据数据库表和实体的对应关系进行结果集封装
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    writeMethod.invoke(o, value);
                }
                objects.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return (T) objects;
    }

    public int update(Configuration configuration, MappedStatement mappedStatement, Object[] params) {
        int row = 0;
        try {
            //1.注册驱动,获取连接
            Connection connection = configuration.getDataSource().getConnection();
            //2.获取sql语句: select * from user where id = #{id} and username = #{username}
            String sql = mappedStatement.getSql();
            //转换sql语句.jdbc不能识别#{id}这种占位符,只会识别select * from user where id = ? and username = ?,
            //我们需要对 #{}进行解析存储
            BoundSql boundSql = getBoundSql(sql);
            //3.获取预处理对象
            PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
            //4.设置参数
            //获取入参的全路径通过反射得到Class对象
            String paramterType = mappedStatement.getParamterType();
            Class<?> classType = getClassType(paramterType);
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            for (int i = 0; i < parameterMappings.size(); i++) {
                String content = parameterMappings.get(i).getContent();
                //通过反射获取实体对象中的值
                Field declaredField = classType.getDeclaredField(content);
                //暴力访问
                declaredField.setAccessible(true);
                Object o = declaredField.get(params[0]);
                preparedStatement.setObject(i + 1, o);
            }
            //5.执行sql
            row = preparedStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return row;
    }

    /**
     * 获取入参的全路径通过反射得到Class对象
     *
     * @param paramterType
     * @return
     */
    private Class<?> getClassType(String paramterType) {
        if (paramterType != null) {
            Class<?> aClass = null;
            try {
                aClass = Class.forName(paramterType);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return aClass;

        }
        return null;
    }

    /**
     * 对 #{}进行解析:1:将#{}使用?代替,2:解析出#{}里面的值进行存储
     *
     * @param sql
     * @return
     */
    private BoundSql getBoundSql(String sql) {
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        //把#{}代替为?的sql
        String parseSql = genericTokenParser.parse(sql);
        //获取#{}中的名称
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        BoundSql boundSql = new BoundSql();
        boundSql.setSql(parseSql);
        boundSql.setParameterMappings(parameterMappings);
        return boundSql;
    }


}
