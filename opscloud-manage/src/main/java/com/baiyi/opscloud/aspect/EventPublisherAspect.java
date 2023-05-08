package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.common.annotation.EventPublisher;
import com.baiyi.opscloud.common.event.NoticeEvent;
import com.baiyi.opscloud.common.event.SimpleEvent;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.factory.business.BusinessServiceFactory;
import com.baiyi.opscloud.factory.business.base.IBusinessService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 发布事件AOP
 *
 * @Author baiyi
 * @Date 2021/8/17 6:07 下午
 * @Version 1.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Aspect
@Component
@RequiredArgsConstructor
public class EventPublisherAspect {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.EventPublisher)")
    public void annotationPoint() {
    }

    @Around("@annotation(eventPublisher)")
    public Object around(ProceedingJoinPoint joinPoint, EventPublisher eventPublisher) throws Throwable {
        Object result = joinPoint.proceed();
        // 后处理事件
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取参数名称
        String[] params = methodSignature.getParameterNames();
        // 获取参数值
        Object[] args = joinPoint.getArgs();

        if (params != null && params.length != 0) {
            if (eventPublisher.value() == BusinessTypeEnum.COMMON) {
                // 通过@BusinessType 获取业务类型
                if (joinPoint.getTarget().getClass().isAnnotationPresent(BusinessType.class)) {
                    BusinessType businessType = joinPoint.getTarget().getClass().getAnnotation(BusinessType.class);
                    Object body = args[0];
                    SimpleEvent simpleEvent = SimpleEvent.builder()
                            .eventType(businessType.value().name())
                            .action(eventPublisher.eventAction().name())
                            .body(body)
                            .build();
                    handlePublishEvent(simpleEvent);
                } else {
                    // 从参数获取
                    publishEventWithParam(args[0], eventPublisher.eventAction().name());
                }
            } else {
                Object body = args[0];
                SimpleEvent simpleEvent = SimpleEvent.builder()
                        .eventType(eventPublisher.value().name())
                        .action(eventPublisher.eventAction().name())
                        .body(body)
                        .build();
                handlePublishEvent(simpleEvent);
            }
        }
        return result;
    }

    @SuppressWarnings("PatternVariableCanBeUsed")
    private void publishEventWithParam(Object message, String action) {
        if (message instanceof BaseBusiness.IBusiness) {
            BaseBusiness.IBusiness ib = (BaseBusiness.IBusiness) message;
            Object body = getBody(ib);
            if (body == null) {
                return;
            }
            SimpleEvent simpleEvent = SimpleEvent.builder()
                    .eventType(Objects.requireNonNull(BusinessTypeEnum.getByType(ib.getBusinessType())).name())
                    .action(action)
                    .body(body)
                    .build();
            handlePublishEvent(simpleEvent);
        }
    }

    private Object getBody(BaseBusiness.IBusiness ib) {
        IBusinessService iBusinessService = BusinessServiceFactory.getIBusinessServiceByBusinessType(ib.getBusinessType());
        if (iBusinessService == null) {
            return null;
        }
        return iBusinessService.getById(ib.getBusinessId());
    }

    private void handlePublishEvent(SimpleEvent simpleEvent) {
        // 发送事件
        applicationEventPublisher.publishEvent(new NoticeEvent(simpleEvent));
    }

}
