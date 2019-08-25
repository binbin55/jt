package com.jt.aop;

import com.jt.anno.Cache_Find;
import com.jt.util.ObjectMapperUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

@Aspect     //表示标识切面    切面=切入点+通知
@Component  //将对象交给spring容器管理
public class CacheAspect {

    @Autowired(required = false)
    private JedisCluster jedis;

    /**
     *  环绕通知:
     *      1.返回值   必须为object类型 标识执行完成业务之后返回用户数据对象
     *      2.参数
     *          2.1 必须位于第一位
     *          2.2 参数类型必须为ProceedingJoinPoint
     *      3.关于注解取值规则:
     *          springAOP中提供了可以直接获取注解的方法,但是要求参数的名称
     *          必须一致,否则映射错误
     */
    @Around("@annotation(cacheFind)")
    public Object around(ProceedingJoinPoint joinPoint, Cache_Find cacheFind) throws Throwable {
        String key = getKey(joinPoint,cacheFind);
        String resultJSON = jedis.get(key);
        Object resultData = null;
        if (StringUtils.isEmpty(resultJSON)){
            resultData = joinPoint.proceed();
            String value = ObjectMapperUtil.toJson(resultData);
            if (cacheFind.seconds()>0){
                jedis.setex(key,cacheFind.seconds(),value);
            }else {
                jedis.set(key,value);
            }
        }else {
            resultData = ObjectMapperUtil.toObject(resultJSON, getType(joinPoint));
        }
        return resultData;
    }

    private Class getType(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getReturnType();
    }

    private String getKey(ProceedingJoinPoint joinPoint, Cache_Find cacheFind) {
        String key = cacheFind.key();
        if (StringUtils.isEmpty(key)){
            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getSignature().getDeclaringTypeName();
            String arg1 = String.valueOf(joinPoint.getArgs()[0]);
            return className + "." + methodName + "::" + arg1;
        } else {
            return key;
        }
    }

}
