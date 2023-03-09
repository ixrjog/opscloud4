package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.domain.annotation.Decrypt;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @Author baiyi
 * @Date 2021/6/16 10:38 上午
 * @Version 1.0
 */
@Aspect
@Component
@RequiredArgsConstructor
public class DecryptAspect {

    private final StringEncryptor stringEncryptor;

    @Pointcut("@annotation(com.baiyi.opscloud.domain.annotation.Decrypt)")
    public void action() {
    }

    @Around(value = "action()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        for (Object requestObj : pjp.getArgs()) {
            // 判断对象是否有 @Decrypt 注解
            if (requestObj.getClass().isAnnotationPresent(Decrypt.class)) {
                handleDecrypt(requestObj);
            }
        }
        return pjp.proceed();
    }

    private void handleDecrypt(Object requestObj) throws IllegalAccessException {
        if (Objects.isNull(requestObj)) {
            return;
        }
        Field[] fields = requestObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 判断对象属性是否有 @Decrypt 注解
            if (field.isAnnotationPresent(Decrypt.class)) {
                field.setAccessible(true);
                String plaintextValue = (String) field.get(requestObj);
                field.set(requestObj, stringEncryptor.decrypt(plaintextValue));
            }
        }
    }
}
