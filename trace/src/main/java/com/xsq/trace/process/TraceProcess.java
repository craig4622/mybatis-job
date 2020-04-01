package com.xsq.trace.process;

import com.alibaba.fastjson.JSON;
import com.xsq.trace.annotation.Trace;
import com.xsq.trace.manager.TraceInheritParamManager;
import com.xsq.trace.manager.TraceParamManager;
import com.xsq.trace.util.TraceUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName TraceProcess
 * @Description TODO
 * @Author xsq
 * @Date 2020/1/13 11:36
 **/
@Slf4j
public class TraceProcess {

    /**
     * The constant TRACE_ID.
     */
    public static final String TRACE_ID = "traceId";

    /**
     * The constant PROCESS_NAME.
     */
    public static final String PROCESS_NAME = "processName";

    public static Object trace(ProceedingJoinPoint proceedingJoinPoint) {
        long start = System.currentTimeMillis();
        Object result = null;
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //处理Trace注解的方法
        if (method.isAnnotationPresent(Trace.class)) {
            Object traceId = TraceInheritParamManager.get(TRACE_ID);
            //获取自定义注解Trace
            Trace traceAnnotation = methodSignature.getMethod().getAnnotation(Trace.class);
            Object processName = TraceInheritParamManager.get(PROCESS_NAME);
            Object globalProcessStep = TraceInheritParamManager.get("processStep");
            if (processName == null) {
                processName = traceAnnotation.processName();
                TraceInheritParamManager.set(PROCESS_NAME, processName);
            }
            if (traceId == null) {
                traceId = UUID.randomUUID();
                TraceInheritParamManager.set(TRACE_ID, traceId);
                TraceParamManager.set(TRACE_ID, traceId);
                TraceParamManager.set(PROCESS_NAME, TraceInheritParamManager.get(PROCESS_NAME));
            }

            if (globalProcessStep == null) {
                globalProcessStep = new AtomicLong(0);
                TraceInheritParamManager.set("processStep", globalProcessStep);
            }

            long thisProcessStepValue = TraceUtil.getThisProcessStepValue(traceId, (AtomicLong) globalProcessStep);

            //创建输入参数字符串以及返回数据字符串

            String argsString = getArgsJson(proceedingJoinPoint.getArgs());

            StringBuilder returnString = new StringBuilder();
            result = getResult(proceedingJoinPoint);
            if (result != null) {
                returnString.append(JSON.toJSONString(result));
            }

            long end = System.currentTimeMillis();

            log.info("threadId:" + Thread.currentThread().getId() +
                    " triceId:" + traceId +
                    " ProcessName:" + processName +
                    " ProcessStep:" + TraceUtil.Long2TraceStepString(thisProcessStepValue) +
                    " Joinpoint:" + proceedingJoinPoint +
                    " argsJson:" + argsString +
                    " returnJson:" + returnString +
                    " cost:" + (end - start) + " ms!");

        }

        return result;
    }


    /**
     * 让目标方法执行,并获取返回值
     *
     * @param joinPoint
     * @return
     */
    private static Object getResult(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            result = joinPoint.proceed();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error(throwable.toString(), throwable);
        }
        return result;
    }

    /**
     * 获取方法入参转为json并拼接
     *
     * @param args
     * @return
     */
    private static String getArgsJson(Object[] args) {
        StringBuilder argsString = new StringBuilder();

        if (args != null && args.length > 0) {
            for (Object arg : args) {
                try {
                    argsString.append(JSON.toJSONString(arg));
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.toString(), e);
                }
            }
        }
        return argsString.toString();

    }
}
