package com.baiyi.opscloud.schedule.quartz.job;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.provider.base.asset.SimpleAssetProvider;
import com.baiyi.opscloud.datasource.packer.DsInstancePacker;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 通用资产拉取任务
 *
 * @Author baiyi
 * @Date 2022/2/21 10:26 AM
 * @Version 1.0
 */
@Slf4j
@Component
public class AssetPullJob extends QuartzJobBean {

    public static final String ASSET_TYPE = "assetType";

    public static final String INSTANCE_ID = "instanceId";

    private static DsInstanceService dsInstanceService;

    private static DsInstancePacker dsInstancePacker;

    @Autowired
    public void setDsInstanceService(DsInstanceService dsInstanceService) {
        AssetPullJob.dsInstanceService = dsInstanceService;
    }

    @Autowired
    public void setDsInstancePacker(DsInstancePacker dsInstancePacker) {
        AssetPullJob.dsInstancePacker = dsInstancePacker;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 获取参数
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        // 任务
        String assetType = jobDataMap.getString(ASSET_TYPE);
        Integer instanceId = jobDataMap.getInt(INSTANCE_ID);
        log.info("Pull asset task: assetType={}, instanceId={}, trigger={}", assetType, instanceId, jobExecutionContext.getTrigger());
        // 任务开始时间
        DsAssetParam.PullAsset pullAsset = DsAssetParam.PullAsset.builder()
                .assetType(assetType)
                .instanceId(instanceId)
                .build();
        try {
            List<SimpleAssetProvider<?>> providers = getProviders(pullAsset.getInstanceId(), pullAsset.getAssetType());
            if (!CollectionUtils.isEmpty(providers)) {
                providers.forEach(x -> x.pullAsset(pullAsset.getInstanceId()));
            }
        } catch (Exception e) {
            log.error("Pull asset task error: assetType={}, instanceId={}, trigger={}, {}", assetType, instanceId, jobExecutionContext.getTrigger(), e.getMessage());
            throw new JobExecutionException(e.getMessage());
        }
    }

    private List<SimpleAssetProvider<?>> getProviders(Integer instanceId, String assetType) {
        DatasourceInstance dsInstance = dsInstanceService.getById(instanceId);
        DsInstanceVO.Instance instance = BeanCopierUtil.copyProperties(dsInstance, DsInstanceVO.Instance.class);
        dsInstancePacker.wrap(instance, SimpleExtend.EXTEND);
        return AssetProviderFactory.getProviders(instance.getInstanceType(), assetType);
    }

}
