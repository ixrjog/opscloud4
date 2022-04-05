package com.baiyi.opscloud.other;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.workorder.validator.QueueValidator;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import javax.validation.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2022/4/5 12:14
 * @Version 1.0
 */
public class OtherTest extends BaseUnit {

    @Resource
    private ValidatorFactory validatorFactory;

    @Resource
    private Validator validator;

    @Resource
    private QueueValidator queueValidator;

    @Data
    @Builder
    @Valid
    public static class ATest {

        @NotNull
        @Size(min = 15, max = 900, message = "AADDBBB")
        private String id;

    }


    @Test
    void dddd() {
        ATest a = ATest.builder()
                .id("1")
                .build();

        ValidatorFactory vF = Validation.buildDefaultValidatorFactory();

        Validator v = validatorFactory.getValidator();

        StringBuilder sb = new StringBuilder();

        Set<ConstraintViolation<ATest>> constraintViolationSet = validator.validate(a);
        constraintViolationSet.forEach(vInfo -> {
            sb.append(vInfo.getMessage());
        });

        print(sb.toString());
    }

    @Test
    void ddd2() {
        Map<String, String> attributes = Maps.newHashMap();
        attributes.put("DelaySeconds", "1");
        attributes.put("MaximumMessageSize", "1");
        attributes.put("MessageRetentionPeriod", "1");
        attributes.put("ReceiveMessageWaitTimeSeconds", "1");
        attributes.put("VisibilityTimeout", "1");
        queueValidator.validate(attributes);
    }

}
