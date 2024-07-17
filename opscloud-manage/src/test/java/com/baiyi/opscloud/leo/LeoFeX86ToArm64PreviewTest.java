package com.baiyi.opscloud.leo;

import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.google.common.base.Splitter;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/16 下午5:23
 * &#064;Version 1.0
 */
public class LeoFeX86ToArm64PreviewTest extends BaseKubernetesTest {

    @Resource
    private ApplicationService applicationService;

    @Resource
    private LeoJobService leoJobService;

    final String previewAppNames = """
            active-agent-points-h5
            active-agent-xmas-h5
            agent-agent-help-h5
            customer-im-h5
            lego-lego-h5
            lego-ssr-h5
            merchant-dispute-h5
            nginx-palmpay-h5
            nginx-palmpay-h5
            open-api-h5
            palm-business-business-app-h5
            palmpay-activities-h5
            palmpay-big-sale-h5
            palmpay-c-h5
            palmpay-card-h5
            palmpay-cash-spree-h5
            palmpay-cashbox-h5
            palmpay-claim-coupons-h5
            palmpay-close-account-h5
            palmpay-crazy-cash-h5
            palmpay-customer-service-h5
            palmpay-early-refund-h5
            palmpay-echat-h5
            palmpay-exchange-rate-h5
            palmpay-flexi-h5
            palmpay-football-h5
            palmpay-grab-deals-h5
            palmpay-h5
            palmpay-instalment-h5
            palmpay-my-trip-h5
            palmpay-palm-safe-h5
            palmpay-palm-zone-h5
            palmpay-plus-h5
            palmpay-qr-card-h5
            palmpay-settings-h5
            pay-with-transfer-ssr-h5
            web-marketing-activities-h5
            web-pay-shop-gh-h5
            web-pay-with-transfer-h5
            web-payment-claim-h5
            """;

    @Test
    void previewTest() {
        Iterable<String> names = Splitter.on("\n").split(previewAppNames);
        names.forEach(e -> {
            if (StringUtils.hasText(e)) {
                Application application = applicationService.getByName(e);
                // prod
                preview(application, 4);
            }
        });
    }

    void preview(Application application, int envType) {
        List<LeoJob> jobs = leoJobService.queryJobWithApplicationId(application.getId()).stream()
                // 过滤环境 + 有效
                .filter(e -> e.getEnvType() == envType && e.getIsActive())
                // 过滤模版是否为 H5模板 id=7
                .filter(e -> e.getTemplateId() == 7).toList();
        if (CollectionUtils.isEmpty(jobs)) {
            // 无jobs退出
            System.out.println(StringFormatter.format("{} 无有效的Jobs.", application.getName()));
            return;
        }
    }

}
