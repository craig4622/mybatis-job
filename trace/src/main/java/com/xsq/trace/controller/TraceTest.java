package com.xsq.trace.controller;

import com.xsq.trace.annotation.Trace;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huke <huke@tiantanhehe.com>
 * @version V1.0
 * @see:
 * @since:
 * @date 2017/2/12
 */
@RestController
@RequestMapping("traceTest")
public class TraceTest {

    @Trace(processName = "test")
    @RequestMapping("test1")
    public void doSomeThing1() {
        System.out.println("doSomeThing1");
    }

    @Trace(processName = "test")
    @RequestMapping("test2")
    public void doSomeThing2() {
        System.out.println("doSomeThing2");
    }

    @Trace(processName = "test")
    @RequestMapping("test3")
    public int doSomeThing3(String test) {
        System.out.println("doSomeThing3" + test);
        return 0;
    }

    @Trace(processName = "test4")
    @RequestMapping("test4")
    public String doSomeThin4g(String test) {
        System.out.println("doSomeThing4" + test);
        return test;
    }



    @RequestMapping("test5")
    public String doSomeThing5(String test) {
        System.out.println("doSomeThing5" + test);
        return test;
    }

}
