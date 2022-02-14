package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.common.util.RegexUtil;
import com.baiyi.opscloud.domain.annotation.DesensitizedField;
import com.baiyi.opscloud.domain.constants.SensitiveTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @Author 修远
 * @Date 2021/6/11 10:56 上午
 * @Since 1.0
 */

@Aspect
@Component
@Slf4j
public class DesensitizeAspect {

    @Pointcut("@annotation(com.baiyi.opscloud.domain.annotation.DesensitizedMethod)")
    public void action() {
    }

    @After(value = "action()")
    public void doAfter(JoinPoint joinPoint) throws IllegalAccessException {
        Object[] args = joinPoint.getArgs();
        for (Object object : args) {
            handleDesensitized(object);
        }
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
                if (StringUtils.isEmpty(value)) return value;
                if (RegexUtil.isPhone(value)) {
                    StringBuilder sb = new StringBuilder(value);
                    return sb.replace(3, 7, "****").toString();
                }
                return value;
            case PASSWORD:
                return Strings.EMPTY;
            default:
                return Strings.EMPTY;
        }
    }
}
