package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.domain.annotation.Encrypt;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @Author 修远
 * @Date 2021/5/18 3:20 下午
 * @Since 1.0
 */

@Aspect
@Component
public class EncryptorAspect {

    @Resource
    private StringEncryptor stringEncryptor;

    @Pointcut("@annotation(com.baiyi.opscloud.domain.annotation.Encrypt)")
    public void action() {
    }

    @Around(value = "action()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object requestObj = pjp.getArgs()[0];
        doEncrypt(requestObj);
        return pjp.proceed();
    }

    private void doEncrypt(Object requestObj) throws IllegalAccessException {
        if (Objects.isNull(requestObj)) {
            return;
        }
        Field[] fields = requestObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Encrypt.class)) {
                field.setAccessible(true);
                String plaintextValue = (String) field.get(requestObj);
                if (StringUtils.isNotBlank(plaintextValue)) {
                    field.set(requestObj, stringEncryptor.encrypt(plaintextValue));
                }
            }
        }
    }

}
