package com.baiyi.caesar.aspect;

import com.baiyi.caesar.domain.annotation.Encrypt;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/5/18 3:20 下午
 * @Since 1.0
 */

@Aspect
@Component
public class EncryptorAspect {

    @Resource
    private StringEncryptor stringEncryptor;

    @Pointcut("@annotation(com.baiyi.caesar.domain.annotation.Encrypt)")
    public void action() {
    }

    @Around(value = "action()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object requestObj = pjp.getArgs()[0];
        handleEncrypt(requestObj);
        return pjp.proceed();
    }
     
    private void handleEncrypt(Object requestObj) throws IllegalAccessException {
        if (Objects.isNull(requestObj)) {
            return;
        }
        Field[] fields = requestObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Encrypt.class)) {
                field.setAccessible(true);
                String plaintextValue = (String) field.get(requestObj);
                field.set(requestObj, stringEncryptor.encrypt(plaintextValue));
            }
        }
    }

}
