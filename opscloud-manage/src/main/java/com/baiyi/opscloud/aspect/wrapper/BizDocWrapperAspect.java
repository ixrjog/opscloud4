package com.baiyi.opscloud.aspect.wrapper;

import com.baiyi.opscloud.common.annotation.BizDocWrapper;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessDocument;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.vo.business.BusinessDocumentVO;
import com.baiyi.opscloud.packer.business.BusinessDocumentPacker;
import com.baiyi.opscloud.service.business.BusinessDocumentService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/5/15 19:29
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
public class BizDocWrapperAspect {

    @Resource
    private BusinessDocumentService bizDocumentService;

    @Resource
    private BusinessDocumentPacker bizDocumentPacker;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.BizDocWrapper)")
    public void annotationPoint() {
    }

    @Around("@annotation(bizDocWrapper)")
    public Object around(ProceedingJoinPoint joinPoint, BizDocWrapper bizDocWrapper) throws OCException {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new OCException(e.getMessage());
        }
        boolean extend = bizDocWrapper.extend();
        BusinessDocumentVO.IBusinessDocument targetDoc = null;
        if (bizDocWrapper.wrapResult()) {
            targetDoc = (BusinessDocumentVO.IBusinessDocument) result;
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
                        extend = ((IExtend) arg).getExtend() != null && ((IExtend) arg).getExtend();
                        continue;
                    }
                }
                if (targetDoc == null) {
                    if (arg instanceof BusinessDocumentVO.IBusinessDocument) {
                        targetDoc = (BusinessDocumentVO.IBusinessDocument) arg;
                    }
                }
            }
        }
        if (extend && targetDoc != null) {
            wrap(targetDoc);
        }
        return result;
    }

    public void wrap(BusinessDocumentVO.IBusinessDocument iBusinessDocument) {
        BusinessDocument doc = bizDocumentService.getByBusiness(iBusinessDocument);
        BusinessDocumentVO.Document document = BeanCopierUtil.copyProperties(doc, BusinessDocumentVO.Document.class);
        bizDocumentPacker.wrap(document, SimpleExtend.EXTEND);
        iBusinessDocument.setDocument(document);
    }

}
