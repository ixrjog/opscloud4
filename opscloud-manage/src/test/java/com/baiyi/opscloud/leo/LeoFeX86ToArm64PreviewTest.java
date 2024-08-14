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
            qa-data-center-h5
            tecno-h5
            leo-h5-demo
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
