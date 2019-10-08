package com.jy23.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RE {
    String only_Chinese="^[\\u4e00-\\u9fa5]{0,}$";
    String only_number="^[0-9]*$";
    String number_letter_underline="^\\w+$";
    String email="\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
    String phone="^1(\\d{10})$";

    String re()  ;
    String msg()  ;
    int code() default -1;
}
