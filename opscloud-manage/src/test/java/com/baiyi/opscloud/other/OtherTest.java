package com.baiyi.opscloud.other;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.alert.rule.impl.ConsulAlertRule;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.facade.datasource.DsInstanceAssetFacade;
import com.baiyi.opscloud.workorder.validator.QueueValidator;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import javax.validation.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
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

    @Resource
    private DsInstanceAssetFacade dsInstanceAssetFacade;

    @Resource
    private ConsulAlertRule consulAlertRule;

    @Data
    @Builder
    @Valid
    public static class ATest {

        @NotNull
        @Size(min = 15, max = 900, message = "AADDBBB")
        private String id;

    }


    @Test
    void test1() {
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
    void test2() {
        // 校验工单条目失败: 消息保留周期应介于1分钟至14天之间、最大消息大小应介于1KB和256KB之间
        Map<String, String> attributes = Maps.newHashMap();
        attributes.put("DelaySeconds", "1");
        attributes.put("MaximumMessageSize", "262144");
        // 60
        attributes.put("MessageRetentionPeriod", "1");
        attributes.put("ReceiveMessageWaitTimeSeconds", "1");
        attributes.put("VisibilityTimeout", "1");
        queueValidator.validate(attributes);
    }

    @Test
    void test3() {
        consulAlertRule.preData();
        DsAssetParam.AssetPageQuery pageQuery = DsAssetParam.AssetPageQuery.builder()
                .assetType("CONSUL_SERVICE")
                .extend(true)
                .instanceId(36)
                .length(5)
                .page(1)
                .queryName("shopping-order")
                .relation(false)
                .build();
        List<DsAssetVO.Asset> assetList = dsInstanceAssetFacade.queryAssetPage(pageQuery).getData();
        assetList.forEach(asset -> consulAlertRule.evaluate(asset));
    }

}
