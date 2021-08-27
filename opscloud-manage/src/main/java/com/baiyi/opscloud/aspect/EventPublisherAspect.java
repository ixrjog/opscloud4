package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.common.annotation.EventPublisher;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.business.BaseBusiness;
import com.baiyi.opscloud.event.NoticeEvent;
import com.baiyi.opscloud.event.SimpleEvent;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.service.server.ServerService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/17 6:07 下午
 * @Version 1.0
 */
@Aspect
@Component
public class EventPublisherAspect {

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private ServerService serverService;

    @Resource
    private ServerGroupService serverGroupService;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.EventPublisher)")
    public void annotationPoint() {
    }

    @Around("@annotation(eventPublisher)")
    public Object around(ProceedingJoinPoint joinPoint, EventPublisher eventPublisher) throws Throwable {
        Object result = joinPoint.proceed();
        // 后处理事件
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] params = methodSignature.getParameterNames();// 获取参数名称
        Object[] args = joinPoint.getArgs();// 获取参数值

        if (params != null && params.length != 0) {
            //Integer businessId = Integer.valueOf(args[0].toString());
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
                    publishEvent(simpleEvent);
                } else {
                    // 从参数获取
                    Object body = args[0];
                    publishEventBusiness(body, eventPublisher.eventAction().name());
                }
            } else {
                Object body = args[0];
                SimpleEvent simpleEvent = SimpleEvent.builder()
                        .eventType(eventPublisher.value().name())
                        .action(eventPublisher.eventAction().name())
                        .body(body)
                        .build();
                publishEvent(simpleEvent);
            }
        }
        return result;
    }

    private void publishEventBusiness(Object message, String action) {
        if (message instanceof BaseBusiness.IBusiness) {
            BaseBusiness.IBusiness ib = (BaseBusiness.IBusiness) message;
            Object body = getBody(ib);
            if (body == null) return;
            SimpleEvent simpleEvent = SimpleEvent.builder()
                    .eventType(BusinessTypeEnum.getByType(ib.getBusinessType()).name())
                    .action(action)
                    .body(body)
                    .build();
            publishEvent(simpleEvent);
        }
    }

    private Object getBody(BaseBusiness.IBusiness ib) {
        if (ib.getBusinessType() == BusinessTypeEnum.SERVER.getType()) {
            return serverService.getById(ib.getBusinessId());
        }
        if (ib.getBusinessType() == BusinessTypeEnum.SERVERGROUP.getType()) {
            return serverGroupService.getById(ib.getBusinessId());
        }
        return null;
    }

    private void publishEvent(SimpleEvent simpleEvent) {
        // 发送事件
        applicationEventPublisher.publishEvent(new NoticeEvent(simpleEvent));
    }
}
