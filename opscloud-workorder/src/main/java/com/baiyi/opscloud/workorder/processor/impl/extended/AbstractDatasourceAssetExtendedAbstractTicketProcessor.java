package com.baiyi.opscloud.workorder.processor.impl.extended;

import com.baiyi.opscloud.common.datasource.base.BaseConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.facade.DsInstanceFacade;
import com.baiyi.opscloud.domain.base.IAssetType;
import com.baiyi.opscloud.domain.base.IInstanceType;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.processor.impl.base.AbstractTicketProcessor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/1/10 9:47 AM
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractDatasourceAssetExtendedAbstractTicketProcessor<T, C extends BaseConfig> extends AbstractTicketProcessor<T> implements IInstanceType, IAssetType {

    @Resource
    protected DsInstanceAssetService dsInstanceAssetService;

    @Resource
    protected DsConfigHelper dsConfigHelper;

    @Resource
    protected DsInstanceFacade dsInstanceFacade;

    abstract protected void processHandle(WorkOrderTicketEntry ticketEntry, T entry) throws TicketProcessException;

    protected void process(WorkOrderTicketEntry ticketEntry, T entry) throws TicketProcessException {
        processHandle(ticketEntry, entry); // 处理
        pullAsset(ticketEntry, entry); // 同步资产
    }

    /**
     * 获取数据源配置
     *
     * @param ticketEntry
     * @param targetClass
     * @return
     */
    protected C getDsConfig(WorkOrderTicketEntry ticketEntry, Class<C> targetClass) {
        DatasourceConfig datasourceConfig = dsConfigHelper.getConfigByInstanceUuid(ticketEntry.getInstanceUuid());
        return dsConfigHelper.build(datasourceConfig, targetClass);
    }

    /**
     * 更新资产
     *
     * @param ticketEntry
     * @param entry
     */
    protected void pullAsset(WorkOrderTicketEntry ticketEntry, T entry) {
        try {
            dsInstanceFacade.pullAsset(ticketEntry.getInstanceUuid(), getAssetType(), entry);
        } catch (Exception e) {
            log.error("推送数据源资产失败: instanceUuid = {} , entry = {}", ticketEntry.getInstanceUuid(), JSONUtil.writeValueAsString(entry));
        }
    }

    @Override
    public String getAssetType() {
        return getKey();
    }

}
