package com.zhifu.aspect;



import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by thinkpad on 2016/7/18.
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger logger= LoggerFactory.getLogger(LogAspect.class);



    @Before("execution(* com.zhifu.controller.IndexController.*(..))")
    public void beforeMethod(){
        logger.info("before method");

    }
    @After("execution(* com.zhifu.controller.IndexController.*(..))")
    public void afterMethod(){
        logger.info("after method");
    }
}
