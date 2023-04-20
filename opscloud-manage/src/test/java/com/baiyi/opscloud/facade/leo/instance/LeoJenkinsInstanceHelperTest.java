package com.baiyi.opscloud.facade.leo.instance;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.leo.handler.build.helper.LeoJenkinsInstanceHelper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/14 15:41
 * @Version 1.0
 */
class LeoJenkinsInstanceHelperTest extends BaseUnit {

    @Resource
    private LeoJenkinsInstanceHelper leoJenkinsInstanceHelper;


    @Test
    void test() {
        List<String> tags = Lists.newArrayList("@Hongkong");
        List<DatasourceInstance> instances = leoJenkinsInstanceHelper.queryAvailableInstancesWithTags(tags);
        print(instances);
    }

}