package com.baiyi.opscloud.leo;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVO;
import com.baiyi.opscloud.facade.leo.LeoJobFacade;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/20 下午3:29
 * &#064;Version 1.0
 */
public class LeoJobTest extends BaseUnit {

    @Resource
    private LeoJobFacade leoJobFacade;


    @Resource
    private LeoJobService leoJobService;

    @Resource
    private BusinessTagService businessTagService;

    /**
     * 开启sonar
     */
    @Test
    void test1() {
        LeoJobParam.JobPageQuery pageQuery = LeoJobParam.JobPageQuery.builder()
                .envType(2)
                .isActive(true)
                .length(300)
                .page(1)
                .templateId(9)
                .extend(false)
                .build();
        DataTable<LeoJobVO.Job> dataTable = leoJobFacade.queryLeoJobPage(pageQuery);
        for (LeoJobVO.Job job : dataTable.getData()) {
            LeoJob leoJob = leoJobService.getById(job.getId());
            LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob);
            Map<String, LeoBaseModel.Parameter> parameterMap = jobConfig.getJob().getParameters().stream().collect(Collectors.toMap(LeoBaseModel.Parameter::getName, a -> a, (k1, k2) -> k1));
            LeoBaseModel.Parameter sonarParameter = LeoBaseModel.Parameter.builder()
                    .name("isSonar")
                    .value("true")
                    .build();
            parameterMap.put("isSonar", sonarParameter);
            if (parameterMap.containsKey("buildCmd")) {
                LeoBaseModel.Parameter buildParameter = parameterMap.get("buildCmd");
                boolean buildType = buildParameter.getValue().endsWith("mvn");
                // maven gradle
                LeoBaseModel.Parameter buildTypeParameter = LeoBaseModel.Parameter.builder()
                        .name("buildType")
                        .value(buildType ? "maven" : "gradle")
                        .build();
                parameterMap.put("buildType", buildTypeParameter);
                jobConfig.getJob().setParameters(parameterMap.keySet().stream().map(parameterMap::get).toList());
                leoJob.setJobConfig(jobConfig.dump());
                leoJobService.update(leoJob);
                System.out.println(leoJob.getName());
            }
        }
    }


    @Test
    void test2() {
        LeoJobParam.JobPageQuery pageQuery = LeoJobParam.JobPageQuery.builder()
                .envType(2)
                .isActive(true)
                .length(300)
                .page(1)
                .templateId(1)
                .extend(false)
                .build();
        DataTable<LeoJobVO.Job> dataTable = leoJobFacade.queryLeoJobPage(pageQuery);
        for (LeoJobVO.Job job : dataTable.getData()) {
            LeoJob leoJob = leoJobService.getById(job.getId());
            LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob);
            Map<String, LeoBaseModel.Parameter> parameterMap = jobConfig.getJob().getParameters().stream().collect(Collectors.toMap(LeoBaseModel.Parameter::getName, a -> a, (k1, k2) -> k1));
            LeoBaseModel.Parameter sonarParameter = LeoBaseModel.Parameter.builder()
                    .name("isSonar")
                    .value("true")
                    .build();
            parameterMap.put("isSonar", sonarParameter);
            jobConfig.getJob().setParameters(parameterMap.keySet().stream().map(parameterMap::get).toList());
            leoJob.setJobConfig(jobConfig.dump());
            leoJobService.update(leoJob);
            System.out.println(leoJob.getName());
        }
    }

    /**
     * 关闭sonar
     */
    @Test
    void test3() {
        LeoJobParam.JobPageQuery pageQuery = LeoJobParam.JobPageQuery.builder()
                .envType(2)
                .isActive(true)
                .length(300)
                .page(1)
                .templateId(9)
                .extend(false)
                .build();
        DataTable<LeoJobVO.Job> dataTable = leoJobFacade.queryLeoJobPage(pageQuery);
        for (LeoJobVO.Job job : dataTable.getData()) {
            LeoJob leoJob = leoJobService.getById(job.getId());
            LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob);
            Map<String, LeoBaseModel.Parameter> parameterMap = jobConfig.getJob().getParameters().stream().collect(Collectors.toMap(LeoBaseModel.Parameter::getName, a -> a, (k1, k2) -> k1));
            LeoBaseModel.Parameter sonarParameter = LeoBaseModel.Parameter.builder()
                    .name("isSonar")
                    .value("false")
                    .build();
            parameterMap.put("isSonar", sonarParameter);
            if (parameterMap.containsKey("buildCmd")) {
                LeoBaseModel.Parameter buildParameter = parameterMap.get("buildCmd");
                boolean buildType = buildParameter.getValue().endsWith("mvn");
                // maven gradle
                LeoBaseModel.Parameter buildTypeParameter = LeoBaseModel.Parameter.builder()
                        .name("buildType")
                        .value(buildType ? "maven" : "gradle")
                        .build();
                parameterMap.put("buildType", buildTypeParameter);
                jobConfig.getJob().setParameters(parameterMap.keySet().stream().map(parameterMap::get).toList());
                leoJob.setJobConfig(jobConfig.dump());
                leoJobService.update(leoJob);
                System.out.println(leoJob.getName());
            }
        }
    }


    @Test
    void test4() {
        LeoJobParam.JobPageQuery pageQuery = LeoJobParam.JobPageQuery.builder()
                .envType(2)
                .isActive(true)
                .length(300)
                .page(1)
                .templateId(1)
                .extend(false)
                .build();
        DataTable<LeoJobVO.Job> dataTable = leoJobFacade.queryLeoJobPage(pageQuery);
        for (LeoJobVO.Job job : dataTable.getData()) {
            LeoJob leoJob = leoJobService.getById(job.getId());
            LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob);
            Map<String, LeoBaseModel.Parameter> parameterMap = jobConfig.getJob().getParameters().stream().collect(Collectors.toMap(LeoBaseModel.Parameter::getName, a -> a, (k1, k2) -> k1));
            LeoBaseModel.Parameter sonarParameter = LeoBaseModel.Parameter.builder()
                    .name("isSonar")
                    .value("false")
                    .build();
            parameterMap.put("isSonar", sonarParameter);
            jobConfig.getJob().setParameters(parameterMap.keySet().stream().map(parameterMap::get).toList());
            leoJob.setJobConfig(jobConfig.dump());
            leoJobService.update(leoJob);
            System.out.println(leoJob.getName());
        }
    }

}
