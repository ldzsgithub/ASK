package com.jy23.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ValidateAspect {

    private  static final String jsonStr = "{\"%s\": \"%s\",\"%s\": %d}";

    public static Object validate(ProceedingJoinPoint joinPoint) throws Exception {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        if (method == null || method.getAnnotation(RequestMapping.class) == null) {
            return null;
        }
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();
        HttpServletResponse response = null;
        if (args != null) {
            for (int i = 0, length = args.length; i < length; i++) {
                Parameter parameter = parameters[i];
                if (parameter.getParameterizedType().getTypeName().equals(HttpServletResponse.class.getName())) {
                    response = (HttpServletResponse) args[i];
                    break;
                }
            }
        }

        if (args != null) {
            for (int i = 0, length = args.length; i < length; i++) {
                Parameter parameter = parameters[i];
                Annotation[] annotations = parameter.getAnnotations();
                if (annotations != null) {
                    for (Annotation an : annotations) {
                        Object[] objects = ValidateKit.check(an, args[i]);
                        if (objects != null) {
                            print(response, String.format(jsonStr, ValidateConfig.msgAlias, objects[0], ValidateConfig.codeAlias, objects[1]));
                            return null;
                        }
                    }
                }
            }
        }
        Object o = null;
        try {
            o = joinPoint.proceed();
        } catch (Throwable throwable) {
        }
        return o;
    }


    private static void print(HttpServletResponse response, String json) throws Exception {
        if (response == null) {
            throw new RuntimeException("未找到参数HttpServletResponse类型参数");
        }
        response.setStatus(HttpStatus.OK.value()); //设置状态码
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); //设置ContentType
        response.setCharacterEncoding("UTF-8"); //避免乱码
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        response.getWriter().write(json);
    }
}
