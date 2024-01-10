package com.baiyi.opscloud.workorder.validator;

import com.baiyi.opscloud.workorder.validator.attribute.QueueAttributes;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/4/5 12:40
 * @Version 1.0
 */
@Component
public class QueueValidator extends AbstractAttributeValidator<QueueAttributes> {

    @Override
    protected QueueAttributes toAttributes(Map<String, String> attributes) {
        return QueueAttributes.toAttributes(attributes);
    }

}