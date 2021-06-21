package com.baiyi.caesar.aspect;

import com.baiyi.caesar.common.util.RegexUtil;
import com.baiyi.caesar.domain.annotation.DesensitizedField;
import com.baiyi.caesar.domain.types.SensitiveTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/11 10:56 上午
 * @Since 1.0
 */

@Aspect
@Component
@Slf4j
public class DesensitizeAspect {

    @Pointcut("@annotation(com.baiyi.caesar.domain.annotation.DesensitizedMethod)")
    public void action() {
    }

    @Around(value = "action()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();
        handleDesensitized(result);
        return result;
    }

    private void handleDesensitized(Object resultObj) throws IllegalAccessException {
        if (Objects.isNull(resultObj)) {
            return;
        }
        Field[] fields = resultObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(DesensitizedField.class)) {
                SensitiveTypeEnum type = field.getAnnotation(DesensitizedField.class).type();
                field.setAccessible(true);
                String value = (String) field.get(resultObj);
                field.set(resultObj, setNewValueForField(value, type));
            }
        }
    }

    private String setNewValueForField(String value, SensitiveTypeEnum type) {
        switch (type) {
            case MOBILE_PHONE:
                if (RegexUtil.isPhone(value)) {
                    StringBuilder sb = new StringBuilder(value);
                    return sb.replace(3, 7, "****").toString();
                }
                return value;
            case PASSWORD:
                return Strings.EMPTY;
        }
        return Strings.EMPTY;
    }
}
