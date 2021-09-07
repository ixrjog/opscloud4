package com.baiyi.opscloud.packer.user;

import com.baiyi.opscloud.common.datasource.AliyunDsInstanceConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.util.RegexUtil;
import com.baiyi.opscloud.datasource.factory.DsConfigFactory;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.SimpleRelation;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.facade.user.base.IUserBusinessPermissionPageQuery;
import com.baiyi.opscloud.facade.user.factory.UserBusinessPermissionFactory;
import com.baiyi.opscloud.packer.auth.AuthRolePacker;
import com.baiyi.opscloud.packer.base.IPacker;
import com.baiyi.opscloud.packer.datasource.DsAssetPacker;
import com.baiyi.opscloud.packer.desensitized.DesensitizedPacker;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.util.ExtendUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/14 10:49 上午
 * @Version 1.0
 */
@Component
public class UserPacker implements IPacker<UserVO.User, User> {

    @Resource
    private AuthRolePacker authRolePacker;

    @Resource
    private UserCredentialPacker userCredentialPacker;

    @Resource
    private DesensitizedPacker<UserVO.User> desensitizedPacker;

    @Resource
    private UserService userService;

    @Resource
    private UserAccessTokenPacker userAccessTokenPacker;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsInstanceService dsInstanceService;

    @Resource
    private DsAssetPacker dsAssetPacker;

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigFactory dsFactory;

    @Override
    public UserVO.User toVO(User user) {
        return BeanCopierUtil.copyProperties(user, UserVO.User.class);
    }

    public List<UserVO.User> wrapVOList(List<User> data) {
        List<UserVO.User> userList = BeanCopierUtil.copyListProperties(data, UserVO.User.class);
        return userList.stream()
                .map(e -> desensitizedPacker.desensitized(e))
                .collect(Collectors.toList());
    }

    public List<UserVO.User> wrapVOList(List<User> data, IExtend iExtend) {
        List<UserVO.User> voList = wrapVOList(data);
        return voList.stream().peek(e -> {
            if (ExtendUtil.isExtend(iExtend)) {
                wrap(e);
            }
        }).collect(Collectors.toList());
    }

    public User toDO(UserVO.User user) {
        User pre = BeanCopierUtil.copyProperties(user, User.class);
        if (!StringUtils.isEmpty(pre.getPassword()))
            RegexUtil.checkPasswordRule(pre.getPassword());
        if (!RegexUtil.isPhone(user.getPhone()))
            pre.setPhone(Strings.EMPTY);
        if (StringUtils.isEmpty(user.getUuid())) {
            pre.setUuid(IdUtil.buildUUID());
        }
        return pre;
    }

    public UserVO.User wrap(User user) {
        return wrap(BeanCopierUtil.copyProperties(user, UserVO.User.class));
    }

    public UserVO.User wrap(UserVO.User user) {
        authRolePacker.wrap(user);
        userCredentialPacker.wrap(user);
        userAccessTokenPacker.wrap(user);
        wrapPermission(user);
        wrapRamUser(user);
        return desensitizedPacker.desensitized(user);
    }

    public void wrap(UserVO.IUser iUser) {
        User user = userService.getByUsername(iUser.getUsername());
        if (user != null)
            iUser.setUser(wrap(user));
    }

    private void wrapPermission(UserVO.User user) {
        Map<Integer, IUserBusinessPermissionPageQuery> context = UserBusinessPermissionFactory.getContext();

        UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery = UserBusinessPermissionParam.UserBusinessPermissionPageQuery
                .builder()
                .userId(user.getId())
                .build();
        pageQuery.setPage(1);
        pageQuery.setLength(10000);

        Map<String, List<UserVO.IUserPermission>> businessPermissions = Maps.newHashMap();

        context.keySet().forEach(k -> {
            DataTable<UserVO.IUserPermission> table = context.get(k).queryUserBusinessPermissionPage(pageQuery);
            BusinessTypeEnum businessTypeEnum = BusinessTypeEnum.getByType(k);
            if (businessTypeEnum != null && !CollectionUtils.isEmpty(table.getData())) {
                businessPermissions.put(businessTypeEnum.name(), table.getData());
            }
        });
        user.setBusinessPermissions(businessPermissions);
    }


    private void wrapRamUser(UserVO.User user) {
        DatasourceInstanceAsset param = DatasourceInstanceAsset.builder()
                .assetType(DsAssetTypeEnum.RAM_USER.name())
                .assetKey(user.getUsername())
                .isActive(true)
                .build();

        List<DatasourceInstanceAsset> data = dsInstanceAssetService.queryAssetByAssetParam(param);
        if (CollectionUtils.isEmpty(data)) return;
        List<DsAssetVO.Asset> assets = dsAssetPacker.wrapVOList(data, SimpleExtend.EXTEND, SimpleRelation.RELATION);
        List<UserVO.RamUser> ramUsers = Lists.newArrayList();
        for (DsAssetVO.Asset asset : assets) {
            DatasourceInstance instance = dsInstanceService.getByUuid(asset.getInstanceUuid());

            DatasourceConfig datasourceConfig = dsConfigService.getById(instance.getConfigId());
            AliyunDsInstanceConfig config = dsFactory.build(datasourceConfig, AliyunDsInstanceConfig.class);
            config.getAliyun().getAccount().getRamLoginUrl();

            List<DsAssetVO.Asset> accessKeys
                    = asset.getTree().containsKey(DsAssetTypeEnum.RAM_ACCESS_KEY.name()) ? asset.getTree().get(DsAssetTypeEnum.RAM_ACCESS_KEY.name()) : Lists.newArrayList();
            List<DsAssetVO.Asset> ramPolicies
                    = asset.getChildren().containsKey(DsAssetTypeEnum.RAM_POLICY.name()) ? asset.getChildren().get(DsAssetTypeEnum.RAM_POLICY.name()) : Lists.newArrayList();
            UserVO.RamUser ramUser = UserVO.RamUser.builder()
                    .username(asset.getAssetKey())
                    .loginUser(asset.getAssetKey() + config.getAliyun().getAccount().getDomain())
                    .loginUrl(config.getAliyun().getAccount().getRamLoginUrl())
                    .instanceName(instance.getInstanceName())
                    .name(asset.getName())
                    .accessKeys(accessKeys)
                    .ramPolicies(ramPolicies)
                    .build();
            ramUsers.add(ramUser);
        }
        user.setRamUsers(ramUsers);
    }


}
