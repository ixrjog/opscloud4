package com.baiyi.opscloud.leo;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.LeoTemplate;
import com.baiyi.opscloud.leo.domain.model.LeoTemplateModel;
import com.baiyi.opscloud.service.leo.LeoTemplateService;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/11/1 14:40
 * @Version 1.0
 */
public class LeoTemplateTest extends BaseUnit {

    @Resource
    private LeoTemplateService templateService;

    @Test
    void test() {
        LeoTemplate leoTemplate = templateService.getById(1);
        LeoTemplateModel.TemplateConfig config = LeoTemplateModel.load(leoTemplate.getTemplateConfig());
        print(config);
    }

}
