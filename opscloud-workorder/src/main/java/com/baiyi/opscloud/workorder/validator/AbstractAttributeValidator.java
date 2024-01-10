package com.baiyi.opscloud.workorder.validator;

import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.google.common.base.Joiner;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/4/5 18:32
 * @Version 1.0
 */
public abstract class AbstractAttributeValidator<IAttributeValidator> {

    @Resource
    private Validator validator;

    public void verify(Map<String, String> attributes) {
        IAttributeValidator iAttributeValidator = toAttributes(attributes);
        Set<ConstraintViolation<IAttributeValidator>> constraintViolationSet = validator.validate(iAttributeValidator);
        List<String> messages = constraintViolationSet.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(messages)) {
            throw new TicketVerifyException("校验工单条目失败: {}", Joiner.on("、").join(messages));
        }
    }

    abstract protected IAttributeValidator toAttributes(Map<String, String> attributes);

}