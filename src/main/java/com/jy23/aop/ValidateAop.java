//package com.jy23.aop;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import com.jy23.exception.MsgException;
//import com.jy23.util.ResultStatusCode;
//import com.jy23.util.ValidateAspect;
//import com.jy23.util.ValidateConfig;
//
//@Component
//@Aspect
//public class ValidateAop {
//    @Pointcut("execution(public * com.jy23.controller..*.*(..))")
//    public void validate(){}
//
//
//    @Around("validate()")
//    public Object doBefore(ProceedingJoinPoint joinPoint) throws MsgException {
//        try {
//            return ValidateAspect.validate(joinPoint);
//        } catch (Throwable e) {
//            throw new MsgException(e.getMessage(),ResultStatusCode.ERROR_SYSTEM.getCode());
//        }
//    }
//
//    @Bean
//    private ValidateConfig config(){
//        ValidateConfig validateConfig = new ValidateConfig();
//        return validateConfig;
//    }
//}
