package com.baiyi.opscloud.facade.leo;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.param.template.BusinessTemplateParam;
import com.baiyi.opscloud.domain.vo.template.BusinessTemplateVO;
import com.baiyi.opscloud.facade.template.TemplateFacade;
import com.google.common.base.Splitter;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/3/6 15:08
 * @Version 1.0
 */
public class LeoServiceTest extends BaseUnit {

    @Resource
    private TemplateFacade templateFacade;

    private static final String x = """
            ng-nomi-channel
            ng-onexbet-channel
            ng-parimatch-channel
            ng-pos-access-channel
            ng-pos-polaris-channel
            ng-pos-upsl-channel
            ng-postpay-channel
            ng-qrios-channel
            ng-smile-channel
            ng-sporty-channel
            ng-stanbic-channel
            ng-sterling-channel
            ng-swiftend-channel
            ng-tripsdotcom-channel
            ng-uba-channel
            ng-uba-new-channel
            ng-up-channel
            ng-wajegame-channel
            ng-wgb-channel
            ng-withhold-channel
            ng-wooshpay-channel
            ng-zenith-channel
            paynet-ng-iso-channel
            paynet-server-mock
            paynet-switch-center
            qa-basic-service
            qa-basic-service-init
            taskmanage-consumer-aws
            trade-mng
            tz-creditinfo-channel
            tz-fasthub-channel
            tz-halotel-channel
            tz-pos-uba-itex-channel
            tz-selcom-channel
            tz-tigopesa-channel
            agent-bff-product
            airtime-product
            base-biz-mng
            channel-file-service
            channel-item-center
            channel-sms-center
            crm-customer-mng
            finance-switch-distribution
            gh-ecg-channel
            gh-ghipss-channel
            gh-gtb-channel
            gh-pos-gtb-channel
            gh-pos-uba-itex-channel
            gh-uba-itex-channel
            ke-cellulantt-channel
            ke-creditinfo-channel
            ng-access-channel
            ng-accessbet-channel
            ng-aedc-channel
            ng-africa365-channel
            ng-airtel-channel
            ng-baxi-channel
            ng-betcorrect-channel
            ng-betking-channel
            ng-betnaija-channel
            ng-betwinner-channel
            ng-blusalt-channel
            ng-buypower-channel
            ng-chowdeck-channel
            ng-coralpay-channel
            ng-credequity-channel
            ng-dispute-channel
            ng-dlocal-channel
            ng-dml-channel
            ng-dojah-channel
            ng-ekedc-channel
            ng-etranzact-channel
            ng-etranzact-inward-channel
            ng-fairmoney-channel
            ng-fcmb-channel
            ng-fdc-channel
            ng-fidelity-channel
            ng-geniex-channel
            ng-globucketdata-channel
            ng-gtb-channel
            ng-habaripay-channel
            ng-hydrogen-channel
            ng-interswitch-channel
            ng-irecharge-channel
            ng-issuer-isw-channel
            ng-jedc-channel
            ng-kuda-channel
            ng-kuda-inward-channel
            ng-mfs-channel
            ng-monobvn-channel
            ng-monokyc-channel
            ng-msport-channel
            ng-mtn-channel
            ng-mtnbucket-channel
            ng-nairabet-channel
            ng-new-buypower-channel
            ng-new-fidelity-channel
            ng-new-mtn-channel
            ng-new-onexbet-channel
            ng-nibss-channel
            ng-nibssqr-channel
            ng-ninemobile-channel
            ng-axa-channel
            ng-pos-gtb-channel
            tz-mpesa-channel
            ke-ipay-channel
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
                .templateId(86)
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

