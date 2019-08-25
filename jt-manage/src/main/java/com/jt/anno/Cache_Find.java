package com.jt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //在运行期生效
@Target(ElementType.METHOD)     //修改方法  该注解对该
public @interface Cache_Find {

    String key() default "";    //用户可以不写,如果为空串

    int seconds() default 0;    //用户设置该数据不需要超时时间,则说明用户自己定义了

}
