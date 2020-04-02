package com.lagou.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.lagou.io.ResoucrcesUtils;
import com.lagou.pojo.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @ClassName: XmlConfigBuilder
 * @Description:
 * @author: xsq
 * @date: 2020年03月28日
 */
public class XmlConfigBuilder {

    private Configuration configuration;

    public XmlConfigBuilder() {
        this.configuration = new Configuration();
    }

    /**
     * 该方法就是解析配置文件并封装到Configuration
     *
     * @param in
     * @return
     */
    public Configuration parseConfig(InputStream in) {
        try {
            Document document = new SAXReader().read(in);
            //对应是sqlMapConfig.xml中的<configuration>标签
            Element element = document.getRootElement();
            //对应是sqlMapConfig.xml中的<property>标签
            List<Element> elementList = element.selectNodes("//property");
            Properties properties = new Properties();
            for (Element el : elementList) {
                String name = el.attributeValue("name");
                String value = el.attributeValue("value");
                properties.setProperty(name, value);
            }
            //druid的连接池,避免JDBC数据库连接的频繁创建和释放,设置xml中解析的数据库配置
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setDriverClassName(properties.getProperty("driverClass"));
            druidDataSource.setUrl(properties.getProperty("jdbcUrl"));
            druidDataSource.setUsername(properties.getProperty("username"));
            druidDataSource.setPassword(properties.getProperty("password"));
            configuration.setDataSource(druidDataSource);
            //mapper.xml解析,获取mapper.xml的路径 --字节输入流-解析
            List<Element> mapperElementList = element.selectNodes("//mapper");
            for (Element el : mapperElementList) {
                String resource = el.attributeValue("resource");
                InputStream inputStream = ResoucrcesUtils.getInputStream(resource);
                XmlMapperBuilder xmlMapperBuilder = new XmlMapperBuilder(configuration);
                xmlMapperBuilder.parse(inputStream);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return configuration;
    }
}
