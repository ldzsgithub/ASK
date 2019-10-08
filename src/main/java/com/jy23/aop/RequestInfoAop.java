//package com.jy23.aop;
//
//import lombok.extern.slf4j.Slf4j;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Slf4j
//@Component
//@Aspect
//public class RequestInfoAop {
//
//    @Pointcut("execution(public * com.jy23.controller..*.*(..))")
//    public void requestInfo() {
//
//    }
//
//    @Around("requestInfo()")
//    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
//        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//        HttpServletRequest request = sra.getRequest();
//
//        String url = request.getRequestURL().toString();
//        String method = request.getMethod();
//        String uri = request.getRequestURI();
//        String queryString = request.getQueryString();
//
//        log.info("---->{}\t{}\t\t\t{}", method, url, (queryString == null ? "无参数" : queryString));
//        // result的值就是被拦截方法的返回值
//        Object result = pjp.proceed();
//        if (result != null) {
//            log.info("<----" + result.toString());
//        }
//
//        return result;
//    }
//}
