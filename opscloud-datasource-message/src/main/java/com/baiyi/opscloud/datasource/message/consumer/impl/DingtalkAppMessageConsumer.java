package com.baiyi.opscloud.datasource.message.consumer.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.DingtalkConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.datasource.dingtalk.driver.DingtalkMessageDriver;
import com.baiyi.opscloud.datasource.dingtalk.param.DingtalkMessageParam;
import com.baiyi.opscloud.datasource.message.consumer.base.AbstractMessageConsumer;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/2 2:56 PM
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DingtalkAppMessageConsumer extends AbstractMessageConsumer<DingtalkConfig.Dingtalk> {

    private final DingtalkMessageDriver dingtalkMessageDriver;

    private DatasourceInstanceAsset findAssetUser(DatasourceInstance instance, User user) {
        List<BusinessAssetRelation> relations = businessAssetRelationService.queryBusinessRelations(BusinessTypeEnum.USER.getType(), user.getId());
        for (BusinessAssetRelation relation : relations) {
            DatasourceInstanceAsset asset = dsInstanceAssetService.getById(relation.getDatasourceInstanceAssetId());
            if (asset.getInstanceUuid().equals(instance.getUuid()) && asset.getAssetType().equals(DsAssetTypeConstants.DINGTALK_USER.name())) {
                return asset;
            }
        }
        throw new OCException("发送消息失败: 用户未绑定钉钉用户无法查找对应userid！username={}", user.getUsername());
    }

    @Override
    public void send(DatasourceInstance instance, User user, MessageTemplate mt, String text) {
        DatasourceInstanceAsset asset = findAssetUser(instance, user);
        DingtalkMessageParam.Markdown markdown = DingtalkMessageParam.Markdown.builder()
                .title(mt.getTitle())
                .text(text)
                .build();
        DingtalkMessageParam.Msg msg = DingtalkMessageParam.Msg.builder()
                .markdown(markdown)
                .build();
        DingtalkMessageParam.AsyncSendMessage message = DingtalkMessageParam.AsyncSendMessage.builder()
                .msg(msg)
                .useridList(asset.getAssetId())
                .build();
        dingtalkMessageDriver.asyncSend(buildConfig(instance), message);
    }

    @Override
    protected DingtalkConfig.Dingtalk buildConfig(DatasourceInstance instance) {
        DatasourceConfig config = getConfig(instance);
        return dsConfigManager.build(config, DingtalkConfig.class).getDingtalk();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.DINGTALK_APP.getName();
    }

}