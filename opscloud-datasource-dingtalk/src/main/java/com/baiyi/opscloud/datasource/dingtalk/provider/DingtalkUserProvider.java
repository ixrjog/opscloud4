package com.baiyi.opscloud.datasource.dingtalk.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.SingleTaskConstants;
import com.baiyi.opscloud.common.datasource.DingtalkConfig;
import com.baiyi.opscloud.common.util.EmailUtil;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.exception.DatasourceProviderException;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.dingtalk.driver.DingtalkUserDriver;
import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkUser;
import com.baiyi.opscloud.datasource.dingtalk.param.DingtalkUserParam;
import com.baiyi.opscloud.datasource.dingtalk.provider.base.AbstractDingtalkAssetProvider;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
    private DingtalkUserDriver dingtalkUserDriver;

    @Resource
    private UserService userService;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.DINGTALK_USER.name();
    }

    private DingtalkConfig.Dingtalk buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, DingtalkConfig.class).getDingtalk();
    }

    @Override
    protected List<DingtalkUser.User> listEntities(DsInstanceContext dsInstanceContext) {
        DingtalkConfig.Dingtalk dingtalk = buildConfig(dsInstanceContext.getDsConfig());
        try {
            Set<Long> deptIdSet = queryDeptSubIds(dsInstanceContext);
            List<DingtalkUser.User> entities = Lists.newArrayList();
            Map<String, DingtalkUser.User> allUserMap = queryAllUserMap(dingtalk, deptIdSet);
            allUserMap.keySet().forEach(k -> {
                DingtalkUser.User user = allUserMap.get(k);
                mapping(user);
                entities.add(user);
            });
            return entities;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DatasourceProviderException(e.getMessage(), e);
        }
    }

    private Map<String, DingtalkUser.User> queryAllUserMap(DingtalkConfig.Dingtalk dingtalk, Set<Long> deptIdSet) {
        Map<String, DingtalkUser.User> allUserMap = Maps.newHashMap();
        deptIdSet.forEach(deptId -> {
            DingtalkUserParam.QueryUserPage queryUserPage = DingtalkUserParam.QueryUserPage.builder()
                    .deptId(deptId)
                    .build();
            DingtalkUser.UserResponse userResponse = dingtalkUserDriver.list(dingtalk, queryUserPage);

            if (CollectionUtils.isEmpty(userResponse.getResult().getList())) {
                return;
            }
            Map<String, DingtalkUser.User> userMap = userResponse
                    .getResult()
                    .getList()
                    .stream()
                    .collect(Collectors.toMap(DingtalkUser.User::getUserid, a -> a, (k1, k2) -> k1));
            allUserMap.putAll(userMap);
            log.info("查询钉钉用户: 部门ID={}, 用户总数={}", deptId, allUserMap.size());
        });
        return allUserMap;
    }

    @Override
    protected Set<Long> queryDeptSubIds(DsInstanceContext dsInstanceContext) {
        DsAssetParam.AssetPageQuery pageQuery = DsAssetParam.AssetPageQuery.builder()
                .instanceId(dsInstanceContext.getDsInstance().getId())
                .instanceUuid(dsInstanceContext.getDsInstance().getUuid())
                .assetType(DsAssetTypeConstants.DINGTALK_DEPARTMENT.name())
                .isActive(true)
                .page(1)
                .length(10000)
                .build();
        DataTable<DatasourceInstanceAsset> dataTable = dsInstanceAssetService.queryPageByParam(pageQuery);
        if (CollectionUtils.isEmpty(dataTable.getData())) {
            return Sets.newHashSet();
        }
        return dataTable.getData().stream().map(e -> Long.valueOf(e.getAssetId()))
                .collect(Collectors.toSet());
    }

    private void mapping(DingtalkUser.User user) {
        // 尝试通过手机号映射
        if (!StringUtils.isEmpty(user.getMobile())) {
            List<User> users = userService.listByPhone(user.getMobile());
            if (!CollectionUtils.isEmpty(users)) {
                user.setUsername(users.getFirst().getUsername());
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
    @SingleTask(name = SingleTaskConstants.PULL_DINGTALK_USER, lockTime = "10m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfName()
                .compareOfKey2()
                .compareOfActive()
                .compareOfDescription()
                .build();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(dingtalkUserProvider);
    }

}