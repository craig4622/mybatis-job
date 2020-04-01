package com.xsq.trace.util;

import com.xsq.trace.manager.TraceCacheManager;
import com.xsq.trace.manager.TraceInheritParamManager;
import com.xsq.trace.manager.TraceParamManager;
import com.xsq.trace.model.TraceTree;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName TraceUtil
 * @Description TODO
 * @Author xsq
 * @Date 2020/1/13 15:08
 **/
public class TraceUtil {

    /**
     * 素数常量
     */
    public static int PRIME_BASE = 127;


    /**
     * 将整形数值装换为流程编号
     *
     * @param processStep the process step
     * @return the string
     */
    public static String Long2TraceStepString(long processStep){
        long step = processStep;
        if(step < 0){
            return null;
        }

        String stepString = "";
        while((step/ PRIME_BASE) > 0){
            stepString = step% PRIME_BASE + "." + stepString;
            step = (step - step% PRIME_BASE)/ PRIME_BASE;
        }
        stepString = step + "." + stepString;

        return stepString;

    }

    /**
     * 根据traceid以及全局的步骤获取当前线程的step值
     *
     * @param traceId           the trace id
     * @param globalProcessStep the global process step
     * @return the this process step value
     */
    public static long getThisProcessStepValue(Object traceId, AtomicLong globalProcessStep) {
        Object localProcessStep;
        long thisProcessStepValue = 0L;
        TraceTree traceTree;

        if ((traceId != null) && (TraceParamManager.get("traceId") == null)) {
            //启动了子线程进行分支流程处理
            TraceParamManager.set("traceId", TraceInheritParamManager.get("traceId"));
            TraceParamManager.set("processName", TraceInheritParamManager.get("processName"));

            AtomicLong thisProcessStep = new AtomicLong(((AtomicLong) TraceInheritParamManager.get("processStep")).get());
            thisProcessStep.updateAndGet(n -> n * PRIME_BASE);
            //获取当前主干流程中子分支的个数
            traceTree = TraceCacheManager.getInstance().getTraceTree((UUID) traceId);
            if (traceTree != null) {

                synchronized (traceTree) {
                    int childNumber = traceTree.getChildNumber(thisProcessStep.get() / PRIME_BASE);
                    thisProcessStep.addAndGet(childNumber + 1);
                    TraceParamManager.set("processStep", thisProcessStep);
                    localProcessStep = TraceParamManager.get("processStep");
                    thisProcessStepValue = ((AtomicLong) localProcessStep).get();
                    traceTree.addTraceNode(Thread.currentThread().getId(), thisProcessStepValue);
                }
            }


        } else {
            //增加全局step，把全局复制给local
            globalProcessStep.incrementAndGet();
//                localProcessStep = new AtomicLong(((AtomicLong)globalProcessStep).get());
            TraceParamManager.set("processStep", new AtomicLong(globalProcessStep.get()));

            localProcessStep = TraceParamManager.get("processStep");
            thisProcessStepValue = ((AtomicLong) localProcessStep).get();
            //如果Trace缓存中不存在该traceid对应的缓存，则创建缓存
            traceTree = TraceCacheManager.getInstance().getTraceTree((UUID) traceId);

            if (traceTree == null) {
                traceTree = new TraceTree();
                TraceCacheManager.getInstance().putTraceTree((UUID) traceId, traceTree);
            }

            synchronized (traceTree) {

                traceTree.addTraceNode(Thread.currentThread().getId(), thisProcessStepValue);
            }

        }
        return thisProcessStepValue;
    }
}
