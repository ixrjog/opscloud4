package com.baiyi.caesar.aspet;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/18 10:34 上午
 * @Version 1.0
 */
@Aspect
@Component
@SuppressWarnings({"unused"})
public class EncryptorAspect {

    @Resource
    private StringEncryptor stringEncryptor;


    @Pointcut("@annotation(com.baiyi.caesar.common.annotation.Encryptor)")
    public void annotationPointcut() {

    }

    @Around("annotationPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] params = methodSignature.getParameterNames();// 获取参数名称
        Object[] args = joinPoint.getArgs();// 获取参数值
//        if (null == params || params.length == 0){
//            String mes = "Using Token annotation, the token parameter is not passed, and the parameter is not valid.";
//            logger.info(mes);
//            throw new Exception(mes);
//        }
//        boolean hasToken = false;
//        int index = 0;
//        for (int i = 0; i < params.length; i++) {
//            if (TOKEN_KEY.equals(params[i])) {
//                hasToken = true;
//                index = i;
//                break;
//            }
//        }
//        if (!hasToken){
//            String mes = "The token parameter is not included in the requested parameter, the parameter is not valid.";
//            logger.info(mes);
//            throw new Exception(mes);
//        }
//        this.checkToken(String.valueOf(args[index]));
        return joinPoint.proceed();
    }


}
