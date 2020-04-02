package com.lagou.io;

import java.io.InputStream;

/**
 * @ClassName: ResoucrcesUtils
 * @Description:
 * @author: xsq
 * @date: 2020年03月28日
 */
public class ResoucrcesUtils {
    /**
     * 根据配置文件路径,将配置文件加载成字节输入流,存入内存中
     *
     * @param path
     * @return
     */
    public static InputStream getInputStream(String path) {
        InputStream inputStream = ResoucrcesUtils.class.getClassLoader().getResourceAsStream(path);
        return inputStream;
    }
}
