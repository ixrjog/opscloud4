package com.baiyi.opscloud.packer.user;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.util.RegexUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessAssetRelation;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetProperty;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.facade.user.base.IUserBusinessPermissionPageQuery;
import com.baiyi.opscloud.facade.user.factory.UserBusinessPermissionFactory;
import com.baiyi.opscloud.packer.auth.AuthRolePacker;
import com.baiyi.opscloud.packer.base.IPacker;
import com.baiyi.opscloud.packer.datasource.DsAssetPacker;
import com.baiyi.opscloud.packer.desensitized.DesensitizedPacker;
import com.baiyi.opscloud.packer.tag.TagPacker;
import com.baiyi.opscloud.packer.user.child.RamUserPacker;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.common.util.ExtendUtil;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserPacker implements IPacker<UserVO.User, User> {

    private final AuthRolePacker authRolePacker;

    private final UserCredentialPacker userCredentialPacker;

    private final DesensitizedPacker<UserVO.User> desensitizedPacker;

    private final UserService userService;

    private final UserAccessTokenPacker userAccessTokenPacker;

    private final RamUserPacker ramUserPacker;

    private final TagPacker tagPacker;

    private final DsAssetPacker dsAssetPacker;

    private final DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    @Resource
    private BusinessAssetRelationService bizAssetRelationService;

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
        ramUserPacker.wrap(user);
        tagPacker.wrap(user);
        // 插入头像
        wrapAvatar(user);
        return desensitizedPacker.desensitized(user);
    }

    /**
     *  从资产获取用户头像并插入
     * @param user
     */
    private void wrapAvatar(UserVO.User user) {
        BaseBusiness.IBusiness iBusiness = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.USER.getType())
                .businessId(user.getId())
                .build();
        List<BusinessAssetRelation> relations = bizAssetRelationService.queryBusinessRelations(iBusiness, DsAssetTypeEnum.DINGTALK_USER.name());
        if (CollectionUtils.isEmpty(relations)) return;
        for (BusinessAssetRelation relation : relations) {
            DatasourceInstanceAssetProperty property =
                    dsInstanceAssetPropertyService.queryByAssetId(relation.getDatasourceInstanceAssetId()).stream().filter(p -> p.getName().equals("avatar")).findFirst().orElse(null);
            if (property != null) {
                user.setAvatar(property.getValue());
                return;
            }
        }
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
                .page(1)
                .length(10000)
                .build();

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


}
