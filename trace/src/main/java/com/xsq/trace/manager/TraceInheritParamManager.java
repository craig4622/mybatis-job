package com.xsq.trace.manager;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TraceInheritParamManager
 * @Description TODO
 * @Author xsq
 * @Date 2020/1/13 14:01
 **/
public class TraceInheritParamManager {

    private static InheritableThreadLocal<Map<String, Object>> traceParams = new InheritableThreadLocal<Map<String, Object>>() {
        @Override
        protected HashMap<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    };

    public static <T> T get(String key) {
        return (T) traceParams.get().get(key);
    }


    public static <T> void set(String key, T t) {
        traceParams.get().put(key, t);
    }

    public static void remove(String key) {
        traceParams.get().remove(key);
    }
}
