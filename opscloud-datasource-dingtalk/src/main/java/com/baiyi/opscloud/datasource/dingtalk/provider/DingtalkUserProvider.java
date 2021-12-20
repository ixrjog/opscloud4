package com.baiyi.opscloud.datasource.dingtalk.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.SingleTaskConstants;
import com.baiyi.opscloud.common.datasource.DingtalkConfig;
import com.baiyi.opscloud.common.util.EmailUtil;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.dingtalk.convert.DingtalkAssetConvert;
import com.baiyi.opscloud.datasource.dingtalk.drive.DingtalkUserDrive;
import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkUser;
import com.baiyi.opscloud.datasource.dingtalk.param.DingtalkUserParam;
import com.baiyi.opscloud.datasource.dingtalk.provider.base.AbstractDingtalkAssetProvider;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/11/30 11:32 上午
 * @Version 1.0
 */
@Component
public class DingtalkUserProvider extends AbstractDingtalkAssetProvider<DingtalkUser.User> {

    @Resource
    private DingtalkUserProvider dingtalkUserProvider;

    @Resource
    private DingtalkUserDrive dingtalkUserDrive;

    @Resource
    private UserService userService;

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.DINGTALK_USER.name();
    }

    private DingtalkConfig.Dingtalk buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, DingtalkConfig.class).getDingtalk();
    }

    @Override
    protected List<DingtalkUser.User> listEntities(DsInstanceContext dsInstanceContext) {
        DingtalkConfig.Dingtalk dingtalk = buildConfig(dsInstanceContext.getDsConfig());
        try {
            Set<Long> deptIdSet = queryDeptSubIds(dsInstanceContext);
            List<DingtalkUser.User> entities = Lists.newArrayList();
            deptIdSet.forEach(deptId -> {
                DingtalkUserParam.QueryUserPage queryUserPage = DingtalkUserParam.QueryUserPage.builder()
                        .deptId(deptId)
                        .build();
                DingtalkUser.UserResponse userResponse = dingtalkUserDrive.list(dingtalk, queryUserPage);
                entities.addAll(userResponse.getResult().getList());
            });
            entities.forEach(e -> {
                if (!StringUtils.isEmpty(e.getMobile())) {
                    if (StringUtils.isEmpty(e.getEmail())) {
                        e.setUsername(e.getName());
                    } else {
                        e.setUsername(EmailUtil.toUsername(e.getEmail()));
                    }
                } else {
                    List<User> users = userService.listByPhone(e.getMobile());
                    if (!CollectionUtils.isEmpty(users)) {
                        e.setUsername(users.get(0).getUsername());
                    } else {
                        if (StringUtils.isEmpty(e.getEmail())) {
                            e.setUsername(e.getName());
                        } else {
                            e.setUsername(EmailUtil.toUsername(e.getEmail()));
                        }
                    }
                }
            });
            return entities;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("查询条目失败");
    }

    @Override
    @SingleTask(name = SingleTaskConstants.PULL_DINGTALK_USER, lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (preAsset.getIsActive() != asset.getIsActive())
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, DingtalkUser.User entity) {
        return DingtalkAssetConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(dingtalkUserProvider);
    }

}
