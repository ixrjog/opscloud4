package com.baiyi.opscloud.workorder.validator;

import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.validator.attribute.QueueAttributes;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/4/5 12:40
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class QueueValidator {

    private final Validator validator;

    public void validate(Map<String, String> attributes) {
        QueueAttributes queueAttributes = QueueAttributes.toAttributes(attributes);
        Set<ConstraintViolation<QueueAttributes>> constraintViolationSet = validator.validate(queueAttributes);
        List<String> messages = constraintViolationSet.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(messages)) {
            throw new TicketVerifyException("校验工单条目失败: " + Joiner.on("、").join(messages));
        }
    }

}
