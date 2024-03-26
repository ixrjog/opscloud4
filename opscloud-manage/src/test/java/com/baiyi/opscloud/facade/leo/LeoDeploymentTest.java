package com.baiyi.opscloud.facade.leo;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.param.template.BusinessTemplateParam;
import com.baiyi.opscloud.domain.vo.template.BusinessTemplateVO;
import com.baiyi.opscloud.facade.template.TemplateFacade;
import com.google.common.base.Splitter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/3/5 17:15
 * @Version 1.0
 */
@Slf4j
public class LeoDeploymentTest extends BaseUnit {

    @Resource
    private TemplateFacade templateFacade;

    /**
     * @Operation(summary = "新增业务模板")
     * @PostMapping(value = "/business/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
     * public HttpResult<BusinessTemplateVO.BusinessTemplate> addBusinessTemplate(@RequestBody @Valid BusinessTemplateParam.BusinessTemplate businessTemplate) {
     * return new HttpResult<>(templateFacade.addBusinessTemplate(businessTemplate));
     * }
     */

    private static final String x = """
            out-transfer
            paystack
            posp-channel
            posp-channel-companion
            posp-channel-encryption
            posp-channel-route
            posp-outway
            query
            self-service
            settlement
            tecno-admin
            tecno-basic-data
            tecno-channel
            tecno-device
            tecno-front
            tecno-m-front
            tecno-mail
            tecno-marketing
            tecno-member
            tecno-message
            tecno-push
            tecno-query
            tecno-sms
            tecno-validator
            tz-channel
            uganda-channel
            visa-channel
            basic-uid-service
            channel-center
            crm-gateway
            data-center
            flexi-bff-product
            flexi-mng
            flutterwave
            ghana
            ke-channel
            m-aa
            m-customer-management
            m-front
            m-workflow
            ng-channel
                        """;

    private static final String VARS_TPL = """
            vars:
              appName: {}
            """;

    @Test
    void startTest() {
        Iterable<String> xxxx = Splitter.on("\n").split(x);

        xxxx.forEach(a -> {
            test(a);
        });

        test("ng-onexbet-channel");
    }

    void test(String appName) {

        // aliyun ack dev
        String uuid = "ed919d9364fb47089a297f5ccf670c2c";

        String vars = StringFormatter.format(VARS_TPL, appName);
        BusinessTemplateParam.BusinessTemplate businessTemplate = BusinessTemplateParam.BusinessTemplate.builder()
                .businessId(0)
                .businessType(5)
                .envType(1)
                .instanceUuid(uuid)
                // 2代
                .templateId(47)
                .vars(vars)
                .build();
        System.out.println(appName);
        try {
            BusinessTemplateVO.BusinessTemplate bizTemplate = templateFacade.addBusinessTemplate(businessTemplate);
            templateFacade.createAssetByBusinessTemplate(bizTemplate.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
