package com.lagou.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.lagou.io.ResoucrcesUtils;
import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import sun.misc.DoubleConsts;

import java.awt.image.SampleModel;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @ClassName: XmlMapperBuilder
 * @Description:
 * @author: xsq
 * @date: 2020年03月28日
 */
public class XmlMapperBuilder {


    private Configuration configuration;

    public XmlMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 该方法就是解析配置文件并封装到Configuration
     *
     * @param in
     * @return
     */
    public void parse(InputStream in) {
        try {
            Document document = new SAXReader().read(in);
            Element element = document.getRootElement();
            String namespace = element.attributeValue("namespace");
            Integer number = 0;
            List<Element> elementList = new ArrayList<Element>();
            elementList.addAll(element.selectNodes("//select"));
            elementList.addAll(element.selectNodes("//insert"));
            elementList.addAll(element.selectNodes("//update"));
            elementList.addAll(element.selectNodes("//delete"));
            for (Element el : elementList) {
                String id = el.attributeValue("id");
                String resultType = el.attributeValue("resultType");
                String paramterType = el.attributeValue("paramterType");
                String sqlText = el.getTextTrim();
                MappedStatement mappedStatement = new MappedStatement();
                mappedStatement.setId(id);
                mappedStatement.setResultType(resultType);
                mappedStatement.setParamterType(paramterType);
                mappedStatement.setSql(sqlText);
                if (el.getName() == "select") {
                    mappedStatement.setSqlCommandType(0);
                } else if (el.getName() == "insert") {
                    mappedStatement.setSqlCommandType(1);
                } else if (el.getName() == "update") {
                    mappedStatement.setSqlCommandType(2);
                } else if (el.getName() == "delete") {
                    mappedStatement.setSqlCommandType(3);
                }
                String statementId = namespace + "." + id;
                configuration.getMappedStatement().put(statementId, mappedStatement);
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
