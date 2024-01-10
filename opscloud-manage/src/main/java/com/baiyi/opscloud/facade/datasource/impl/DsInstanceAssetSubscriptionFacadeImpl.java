package com.baiyi.opscloud.facade.datasource.impl;

import com.baiyi.opscloud.common.datasource.AnsibleConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.YamlUtil;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IOUtil;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.base.common.SimpleDsInstanceProvider;
import com.baiyi.opscloud.core.util.SystemEnvUtil;
import com.baiyi.opscloud.datasource.ansible.builder.AnsiblePlaybookArgumentsBuilder;
import com.baiyi.opscloud.datasource.ansible.builder.args.AnsiblePlaybookArgs;
import com.baiyi.opscloud.datasource.ansible.entity.AnsibleExecuteResult;
import com.baiyi.opscloud.datasource.ansible.executor.AnsibleExecutor;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetSubscription;
import com.baiyi.opscloud.domain.param.datasource.DsAssetSubscriptionParam;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetSubscriptionVO;
import com.baiyi.opscloud.facade.datasource.DsInstanceAssetSubscriptionFacade;
import com.baiyi.opscloud.packer.datasource.DsAssetSubscriptionPacker;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetSubscriptionService;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/8/27 4:05 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class DsInstanceAssetSubscriptionFacadeImpl extends SimpleDsInstanceProvider implements DsInstanceAssetSubscriptionFacade {

    @Resource
    private DsAssetSubscriptionPacker dsAssetSubscriptionPacker;

    @Resource
    private DsInstanceAssetSubscriptionService dsInstanceAssetSubscriptionService;

    @Resource
    private DsConfigManager dsConfigManager;

    @Override
    public DataTable<DsAssetSubscriptionVO.AssetSubscription> queryAssetSubscriptionPage(DsAssetSubscriptionParam.AssetSubscriptionPageQuery pageQuery) {
        DataTable<DatasourceInstanceAssetSubscription> table = dsInstanceAssetSubscriptionService.queryPageByParam(pageQuery);

        List<DsAssetSubscriptionVO.AssetSubscription> data = BeanCopierUtil.copyListProperties(table.getData(), DsAssetSubscriptionVO.AssetSubscription.class)
                .stream().peek(e -> dsAssetSubscriptionPacker.wrap(e, pageQuery)).collect(Collectors.toList());

        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void updateAssetSubscription(DsAssetSubscriptionParam.AssetSubscription assetSubscription) {
        DatasourceInstanceAssetSubscription pre = BeanCopierUtil.copyProperties(assetSubscription, DatasourceInstanceAssetSubscription.class);
        DatasourceInstanceAssetSubscription datasourceInstanceAssetSubscription = dsInstanceAssetSubscriptionService.getById(assetSubscription.getId());
        datasourceInstanceAssetSubscription.setPlaybook(pre.getPlaybook());
        datasourceInstanceAssetSubscription.setVars(pre.getVars());
        datasourceInstanceAssetSubscription.setComment(pre.getComment());
        dsInstanceAssetSubscriptionService.update(datasourceInstanceAssetSubscription);
        saveSubscriptionPlaybookFile(datasourceInstanceAssetSubscription);
    }

    /**
     * 发布
     *
     * @param id
     */
    @Override
    public void publishAssetSubscriptionById(int id) {
        DatasourceInstanceAssetSubscription datasourceInstanceAssetSubscription = dsInstanceAssetSubscriptionService.getById(id);
        publishAssetSubscription(datasourceInstanceAssetSubscription);
    }

    @Override
    public void publishAssetSubscription(DatasourceInstanceAssetSubscription datasourceInstanceAssetSubscription) {
        DsInstanceContext instanceContext = buildDsInstanceContext(datasourceInstanceAssetSubscription.getInstanceUuid());
        AnsibleConfig.Ansible ansible = dsConfigManager.build(instanceContext.getDsConfig(), AnsibleConfig.class).getAnsible();
        AnsiblePlaybookArgs args = AnsiblePlaybookArgs.builder()
                .extraVars(YamlUtil.loadVars(datasourceInstanceAssetSubscription.getVars()).getVars())
                .keyFile(SystemEnvUtil.renderEnvHome(ansible.getPrivateKey()))
                .playbook(toSubscriptionPlaybookFile(ansible, datasourceInstanceAssetSubscription))
                .inventory(SystemEnvUtil.renderEnvHome(ansible.getInventoryHost()))
                .build();
        CommandLine commandLine = AnsiblePlaybookArgumentsBuilder.build(ansible, args);
        AnsibleExecuteResult er = AnsibleExecutor.execute(commandLine, NewTimeUtil.MINUTE_TIME * 2);
        Optional.ofNullable(er)
                .map(AnsibleExecuteResult::getOutput)
                .orElseThrow(() -> new OCException("AnsibleExecuteResult 不存在！"));
        try {
            datasourceInstanceAssetSubscription.setLastSubscriptionLog(er.getOutput().toString("utf8"));
            datasourceInstanceAssetSubscription.setLastSubscriptionTime(new Date());
            dsInstanceAssetSubscriptionService.update(datasourceInstanceAssetSubscription);
        } catch (UnsupportedEncodingException e) {
            log.error("发布订阅任务失败: id={}", datasourceInstanceAssetSubscription.getId());
        }
    }

    @Override
    public void addAssetSubscription(DsAssetSubscriptionParam.AssetSubscription assetSubscription) {
        DatasourceInstanceAssetSubscription pre = BeanCopierUtil.copyProperties(assetSubscription, DatasourceInstanceAssetSubscription.class);
        dsInstanceAssetSubscriptionService.add(pre);
        saveSubscriptionPlaybookFile(pre);
    }

    private void saveSubscriptionPlaybookFile(DatasourceInstanceAssetSubscription subscription) {
        if (StringUtils.isEmpty(subscription.getPlaybook())) {
            return;
        }
        String file = toSubscriptionPlaybookFile(subscription);
        IOUtil.writeFile(subscription.getPlaybook(), file);
    }

    /**
     * 转换订阅配置剧本文件
     *
     * @param subscription
     * @return
     */
    public String toSubscriptionPlaybookFile(DatasourceInstanceAssetSubscription subscription) {
        DsInstanceContext instanceContext = buildDsInstanceContext(subscription.getInstanceUuid());
        AnsibleConfig.Ansible ansible = dsConfigManager.build(instanceContext.getDsConfig(), AnsibleConfig.class).getAnsible();
        return toSubscriptionPlaybookFile(ansible, subscription);
    }

    private String toSubscriptionPlaybookFile(AnsibleConfig.Ansible ansible, DatasourceInstanceAssetSubscription subscription) {
        String fileName = Joiner.on("_").join(subscription.getInstanceUuid(), subscription.getDatasourceInstanceAssetId(), subscription.getId()) + ".yml";
        String path = Joiner.on("/").join(ansible.getData(), "subscription", fileName);
        return SystemEnvUtil.renderEnvHome(path);
    }

    @Override
    public void deleteAssetSubscriptionById(int id) {
        dsInstanceAssetSubscriptionService.deleteById(id);
    }

}
