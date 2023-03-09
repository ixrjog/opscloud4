package com.baiyi.opscloud.aspect.wrapper;

import com.baiyi.opscloud.common.annotation.EnvWrapper;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.service.sys.EnvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * Env 包装器
 *
 * @Author baiyi
 * @Date 2022/2/9 9:14 AM
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class EnvWrapperAspect {

    private final EnvService envService;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.EnvWrapper)")
    public void annotationPoint() {
    }

    @Around("@annotation(envWrapper)")
    public Object around(ProceedingJoinPoint joinPoint, EnvWrapper envWrapper) throws OCException {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new OCException(e.getMessage());
        }
        boolean extend = envWrapper.extend();
        EnvVO.IEnv targetEnv = null;
        if (envWrapper.wrapResult()) {
            targetEnv = (EnvVO.IEnv) result;
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取参数名称
        String[] params = methodSignature.getParameterNames();
        // 获取参数值
        Object[] args = joinPoint.getArgs();
        if (params != null && params.length != 0) {
            for (Object arg : args) {
                if (!extend) {
                    if (arg instanceof IExtend) {
                        extend = ((IExtend) arg).getExtend();
                        continue;
                    }
                }
                if (targetEnv == null) {
                    if (arg instanceof EnvVO.IEnv) {
                        targetEnv = (EnvVO.IEnv) arg;
                    }
                }
            }
        }
        if (extend && targetEnv != null) {
            wrap(targetEnv);
        }
        return result;
    }

    public void wrap(EnvVO.IEnv iEnv) {
        Env env = envService.getByEnvType(iEnv.getEnvType());
        iEnv.setEnv(BeanCopierUtil.copyProperties(env, EnvVO.Env.class));
    }

}
