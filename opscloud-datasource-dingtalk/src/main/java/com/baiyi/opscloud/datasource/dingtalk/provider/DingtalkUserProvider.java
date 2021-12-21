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
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/11/30 11:32 上午
 * @Version 1.0
 */
@Slf4j
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
            Map<String, DingtalkUser.User> allUserMap = Maps.newHashMap();

            deptIdSet.forEach(deptId -> {
                DingtalkUserParam.QueryUserPage queryUserPage = DingtalkUserParam.QueryUserPage.builder()
                        .deptId(deptId)
                        .build();
                DingtalkUser.UserResponse userResponse = dingtalkUserDrive.list(dingtalk, queryUserPage);

                if (CollectionUtils.isEmpty(userResponse.getResult().getList())) return;
                Map<String, DingtalkUser.User> userMap = userResponse.getResult().getList().stream().collect(Collectors.toMap(DingtalkUser.User::getUserid, a -> a, (k1, k2) -> k1));
                allUserMap.putAll(userMap);
                log.info("查询钉钉用户: size = {}", allUserMap.size());
            });
            allUserMap.keySet().forEach(k -> {
                DingtalkUser.User user = allUserMap.get(k);
                mapping(user);
                entities.add(user);
            });
            return entities;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("查询条目失败!");
    }

    private void mapping(DingtalkUser.User user) {
        // 尝试通过手机号映射
        if (!StringUtils.isEmpty(user.getMobile())) {
            List<User> users = userService.listByPhone(user.getMobile());
            if (!CollectionUtils.isEmpty(users)) {
                user.setUsername(users.get(0).getUsername());
                return;
            }
        }
        if (StringUtils.isEmpty(user.getEmail())) {
            user.setUsername(user.getName());
        } else {
            user.setUsername(EmailUtil.toUsername(user.getEmail()));
        }
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
