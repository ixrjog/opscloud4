package com.baiyi.opscloud.workorder.processor.impl.extended;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.facade.DsInstanceFacade;
import com.baiyi.opscloud.domain.base.IAssetType;
import com.baiyi.opscloud.domain.base.IInstanceType;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.base.BaseTicketProcessor;
import lombok.extern.slf4j.Slf4j;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/1/10 9:47 AM
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractDsAssetExtendedBaseTicketProcessor<T, C extends BaseDsConfig> extends BaseTicketProcessor<T>
        implements IInstanceType, IAssetType {

    @Resource
    protected DsInstanceAssetService dsInstanceAssetService;

    @Resource
    protected DsConfigManager dsConfigManager;

    @Resource
    protected DsInstanceFacade<T> dsInstanceFacade;

    @Resource
    private WorkOrderTicketService workOrderTicketService;

    protected WorkOrderTicket getTicketById(int ticketId) {
        return workOrderTicketService.getById(ticketId);
    }

    protected DatasourceInstanceAsset getAsset(DatasourceInstanceAsset queryParam) throws TicketVerifyException {
        try {
            DatasourceInstanceAsset asset = dsInstanceAssetService.getByUniqueKey(queryParam);
            if (asset == null) {
                throw new TicketVerifyException("校验工单条目失败: 授权资产不存在！");
            }
            return asset;
        } catch (Exception e) {
            throw new TicketVerifyException("查询授权资产错误: {}", e.getMessage());
        }
    }

    abstract protected void processHandle(WorkOrderTicketEntry ticketEntry, T entry) throws TicketProcessException;

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, T entry) throws TicketProcessException {
        // 处理
        processHandle(ticketEntry, entry);
        // 同步资产
        pullAsset(ticketEntry, entry);
    }

    /**
     * 获取数据源配置
     *
     * @param ticketEntry
     * @param targetClass
     * @return
     */
    protected C getDsConfig(WorkOrderTicketEntry ticketEntry, Class<C> targetClass) {
        DatasourceConfig datasourceConfig = dsConfigManager.getConfigByInstanceUuid(ticketEntry.getInstanceUuid());
        return dsConfigManager.build(datasourceConfig, targetClass);
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
            log.error("推送数据源资产失败: instanceUuid={}, entry={}", ticketEntry.getInstanceUuid(), JSONUtil.writeValueAsString(entry));
        }
    }

    @Override
    public String getAssetType() {
        return getKey();
    }

}