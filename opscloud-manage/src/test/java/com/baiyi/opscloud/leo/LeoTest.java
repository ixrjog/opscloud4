package com.baiyi.opscloud.leo;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.service.leo.LeoJobService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

/**
 * @Author baiyi
 * @Date 2023/10/12 13:39
 * @Version 1.0
 */
public class LeoTest extends BaseUnit {

    @Resource
    private LeoJobService leoJobService;

    @Test
    void test() {
        int page = 1;
        while (true) {
            LeoJobParam.JobPageQuery pageQuery = LeoJobParam.JobPageQuery.builder()
                    .page(page)
                    .length(10)
                    .isActive(true)
                    .build();
            DataTable<LeoJob> dataTable = leoJobService.queryJobPage(pageQuery);
            if (CollectionUtils.isEmpty(dataTable.getData())) {
                return;
            }
            dataTable.getData().forEach(job -> {
               // print(job.getJobConfig());
                String jobConfig = job.getJobConfig();
                if (jobConfig.contains("-DskipTests")) {
                    job.setJobConfig(jobConfig.replace("-DskipTests", "-Dmaven.test.skip=true"));
                    leoJobService.update(job);
                    print("更新任务: name=" + job.getName());
                }
            });
            print("当前分页 "+ page);
            page++;
        }

    }


}
