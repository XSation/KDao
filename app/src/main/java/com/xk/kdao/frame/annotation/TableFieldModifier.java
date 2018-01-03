package com.xk.kdao.frame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于注解实体类的字段，描述某个字段的修饰符
 * Created by xuekai on 2018/1/2.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableFieldModifier {
    String[] value();
}
