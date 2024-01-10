package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Tag;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.tag.TagParam;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/2/9 10:56 AM
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TagsWrapperAspect {

    private final TagService tagService;

    private final BusinessTagService businessTagService;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.TagsWrapper)")
    public void annotationPoint() {
    }

    @Around("@annotation(tagsWrapper)")
    public Object around(ProceedingJoinPoint joinPoint, TagsWrapper tagsWrapper) throws OCException {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new OCException(e.getMessage());
        }
        boolean extend = tagsWrapper.extend();
        TagVO.ITags targetTags = null;
        if (tagsWrapper.wrapResult()) {
            targetTags = (TagVO.ITags) result;
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
                if (targetTags == null) {
                    if (arg instanceof TagVO.ITags) {
                        targetTags = (TagVO.ITags) arg;
                    }
                }
            }
        }
        if (extend && targetTags != null) {
            wrap(targetTags);
        }
        return result;
    }

    private void wrap(TagVO.ITags iTags) {
        TagParam.BusinessQuery queryParam = TagParam.BusinessQuery.builder()
                .businessType(iTags.getBusinessType())
                .businessId(iTags.getBusinessId())
                .build();
        List<Tag> tags = queryBusinessTagByParam(queryParam);
        iTags.setTags(wrapVOList(tags));
    }

    private List<Tag> queryBusinessTagByParam(TagParam.BusinessQuery queryParam) {
        return tagService.queryBusinessTagByParam(queryParam);
    }

    private List<TagVO.Tag> wrapVOList(List<Tag> data) {
        return data.stream().map(e -> {
            TagVO.Tag tag = BeanCopierUtil.copyProperties(e, TagVO.Tag.class);
            tag.setBusinessTypeEnum(BusinessTypeEnum.getByType(e.getBusinessType()));
            tag.setQuantityUsed(businessTagService.countByTagId(tag.getId()));
            return tag;
        }).collect(Collectors.toList());
    }

}