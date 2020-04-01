package com.xsq.trace.aop;

import com.xsq.trace.process.TraceProcess;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @ClassName TraceAspect
 * @Description TODO aop对对应方法进行需要的处理
 * @Author xsq
 * @Date 2020/1/13 10:53
 **/
@Slf4j
@Component
@Aspect
public class TraceAspect {
    /**
     * 表示匹配com.xsq.trace.controller包下的所有方法
     */
    @Pointcut("execution(* com.xsq.trace.controller..*(..))")
    public void controllerAspect(){
        log.info("TraceAspect is invoked");
    }


    /**
     * 对方法的前后进行处理
     */
    @Around("controllerAspect()")
    public Object  doTraceAround(ProceedingJoinPoint  proceedingJoinPoint){
        return TraceProcess.trace(proceedingJoinPoint);
    }
}
