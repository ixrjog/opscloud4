package com.baiyi.opscloud.facade.user.impl;

import com.baiyi.opscloud.common.base.AccessLevel;
import com.baiyi.opscloud.common.builder.SimpleDictBuilder;
import com.baiyi.opscloud.common.constants.enums.UserCredentialTypeEnum;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.*;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.annotation.AssetBusinessRelation;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.annotation.RevokeUserPermission;
import com.baiyi.opscloud.domain.annotation.TagClear;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.SimpleRelation;
import com.baiyi.opscloud.domain.param.auth.LoginParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.server.ServerTreeVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.domain.vo.user.AccessManagementVO;
import com.baiyi.opscloud.domain.vo.user.AccessTokenVO;
import com.baiyi.opscloud.domain.vo.user.UserPermissionVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.domain.vo.user.mfa.MfaVO;
import com.baiyi.opscloud.facade.UserCredentialFacade;
import com.baiyi.opscloud.facade.auth.mfa.MfaAuthHelper;
import com.baiyi.opscloud.facade.server.ServerFacade;
import com.baiyi.opscloud.facade.server.ServerGroupFacade;
import com.baiyi.opscloud.facade.user.UserFacade;
import com.baiyi.opscloud.facade.user.UserPermissionFacade;
import com.baiyi.opscloud.facade.user.base.IUserBusinessPermissionPageQuery;
import com.baiyi.opscloud.facade.user.converter.UserConverter;
import com.baiyi.opscloud.facade.user.factory.UserBusinessPermissionFactory;
import com.baiyi.opscloud.facade.user.handler.UserHandler;
import com.baiyi.opscloud.otp.OtpUtil;
import com.baiyi.opscloud.packer.datasource.DsAssetPacker;
import com.baiyi.opscloud.packer.user.UserPacker;
import com.baiyi.opscloud.packer.user.am.AccessManagementPacker;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.user.*;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.config.ThreadPoolTaskConfiguration.TaskPools.CORE;

/**
 * @Author baiyi
 * @Date 2021/5/14 10:38 上午
 * @Version 1.0
 */
@BusinessType(BusinessTypeEnum.USER)
@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;

    private final AccessTokenService accessTokenService;

    private final UserPacker userPacker;

    private final ServerGroupFacade serverGroupFacade;

    private final ServerFacade serverFacade;

    private final UserPermissionFacade userPermissionFacade;

    private final DsInstanceAssetService dsInstanceAssetService;

    private final DsAssetPacker dsAssetPacker;

    private final UserGroupService userGroupService;

    private final UserPermissionService userPermissionService;

    private final AccessManagementPacker amPacker;

    private final UserCredentialFacade userCredentialFacade;

    private final UserCredentialService userCredentialService;

    private final MfaAuthHelper mfaAuthHelper;

    private final UserHandler userHandler;

    @Override
    public DataTable<UserVO.User> queryUserPage(UserParam.UserPageQuery pageQuery) {
        DataTable<User> table = userService.queryPageByParam(pageQuery);
        List<UserVO.User> data = BeanCopierUtil.copyListProperties(table.getData(), UserVO.User.class)
                .stream()
                .peek(e -> userPacker.wrap(e, pageQuery)).
                collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public UserVO.User getUserDetailsByUsername(String username) {
        User user = userService.getByUsername(StringUtils.isEmpty(username) ? SessionUtil.getUsername() : username);
        UserVO.User userVO = BeanCopierUtil.copyProperties(user, UserVO.User.class);
        userPacker.wrap(userVO, SimpleExtend.EXTEND);
        return userVO;
    }

    @Async(value = CORE)
    @Override
    public void syncUserPermissionGroupForAsset() {
        List<User> users = userService.queryAll();
        if (CollectionUtils.isEmpty(users)) return;
        users.forEach(u -> {
            log.info("同步用户 {}", u.getUsername());
            DatasourceInstanceAsset query = DatasourceInstanceAsset.builder()
                    .assetId(u.getUsername())
                    .assetType(DsAssetTypeConstants.USER.name())
                    .isActive(true)
                    .build();
            List<DatasourceInstanceAsset> userAssets = dsInstanceAssetService.queryAssetByAssetParam(query);
            if (CollectionUtils.isEmpty(userAssets)) return;
            userAssets.forEach(a -> {
                DsAssetVO.Asset asset = BeanCopierUtil.copyProperties(a, DsAssetVO.Asset.class);
                dsAssetPacker.wrap(asset, SimpleExtend.EXTEND, SimpleRelation.RELATION);
                if (asset.getChildren().containsKey(DsAssetTypeConstants.GROUP.name())) {
                    // GROUP存在
                    asset.getChildren().get(DsAssetTypeConstants.GROUP.name()).forEach(g ->
                            userPermissionUserGroup(u, g.getAssetId()));
                }
            });
        });
    }

    private void userPermissionUserGroup(User user, String userGroupName) {
        UserGroup userGroup = userGroupService.getByName(userGroupName);
        if (userGroup == null) return;

        UserPermission userPermission = UserPermission.builder()
                .userId(user.getId())
                .businessType(BusinessTypeEnum.USERGROUP.getType())
                .businessId(userGroup.getId())
                .build();
        try {
            userPermissionService.add(userPermission);
        } catch (Exception ignored) {
        }
    }

    @Override
    @AssetBusinessRelation
    public UserVO.User addUser(UserParam.CreateUser createUser) {
        User preCreateUser = UserConverter.toDO(createUser);
        userService.add(preCreateUser);
        if (BooleanUtils.isTrue(createUser.getNeedInitializeDefaultConfiguration())) {
            userHandler.postCreateUserHandle(preCreateUser);
        }
        // 给切面提供businessId
        createUser.setId(preCreateUser.getId());
        UserVO.User userVO = BeanCopierUtil.copyProperties(createUser, UserVO.User.class);
        userPacker.wrap(userVO, SimpleExtend.EXTEND);
        return userVO;
    }

    /**
     * 此处增强鉴权，只有管理员可以修改其他用户信息，否则只能修改本人信息
     *
     * @param updateUser
     */
    @Override
    public void updateUser(UserParam.UpdateUser updateUser) {
        User checkUser = userService.getById(updateUser.getId());
        if (!checkUser.getUsername().equals(SessionUtil.getUsername())) {
            int accessLevel = userPermissionFacade.getUserAccessLevel(SessionUtil.getUsername());
            if (accessLevel < AccessLevel.OPS.getLevel()) {
                throw new CommonRuntimeException("权限不足: 需要管理员才能修改其他用户信息!");
            }
        }
        User preUpdateUser = UserConverter.toDO(updateUser);
        updateUser.setUsername(checkUser.getUsername());
        userService.updateBySelective(preUpdateUser);
    }

    @Override
    public void setUserActive(String username) {
        User user = userService.getByUsername(username);
        if (user.getIsActive()) {
            userService.setInactive(user);
        } else {
            userService.setActive(user);
        }
    }

    @RevokeUserPermission // 撤销用户的所有授权信息
    @TagClear
    @Override
    public void deleteUser(Integer id) {
        User user = userService.getById(id);
        if (user == null) return;
        if (user.getIsActive()) {
            throw new CommonRuntimeException("当前用户为活跃状态不能删除！");
        }
        userService.delete(user);
    }

    @Override
    public ServerTreeVO.ServerTree queryUserServerTree(ServerGroupParam.UserServerTreeQuery queryParam) {
        User user = userService.getByUsername(SessionUtil.getUsername());
        queryParam.setUserId(user.getId());
        return serverGroupFacade.queryServerTree(queryParam, user);
    }

    @Override
    public DataTable<UserVO.IUserPermission> queryUserBusinessPermissionPage(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery) {
        IUserBusinessPermissionPageQuery iQuery = UserBusinessPermissionFactory.getByBusinessType(pageQuery.getBusinessType());
        if (iQuery != null)
            return iQuery.queryUserBusinessPermissionPage(pageQuery);
        throw new CommonRuntimeException(ErrorEnum.USER_BUSINESS_TYPE_ERROR);
    }

    @Override
    public DataTable<ServerVO.Server> queryUserRemoteServerPage(ServerParam.UserRemoteServerPageQuery queryParam) {
        User user = userService.getByUsername(SessionUtil.getUsername());
        queryParam.setUserId(user.getId());
        return serverFacade.queryUserRemoteServerPage(queryParam);
    }

    @Override
    public AccessTokenVO.AccessToken grantUserAccessToken(AccessTokenVO.AccessToken accessToken) {
        AccessToken pre = AccessToken.builder()
                .username(SessionUtil.getUsername())
                .tokenId(IdUtil.buildUUID())
                .token(PasswordUtil.generatorPassword(32, false))
                .expiredTime(accessToken.getExpiredTime())
                .comment(accessToken.getComment())
                .build();
        accessTokenService.add(pre);
        return BeanCopierUtil.copyProperties(pre, AccessTokenVO.AccessToken.class);
    }

    @Override
    public void revokeUserAccessToken(String tokenId) {
        AccessToken at = accessTokenService.getByTokenId(tokenId);
        if (at == null) return;
        at.setValid(false);
        accessTokenService.update(at);
    }

    @Override
    public DataTable<UserVO.User> queryBusinessPermissionUserPage(UserBusinessPermissionParam.BusinessPermissionUserPageQuery pageQuery) {
        DataTable<User> table = userService.queryPageByParam(pageQuery);
        List<UserVO.User> data = BeanCopierUtil.copyListProperties(table.getData(), UserVO.User.class).stream().peek(e -> {
            userPacker.wrap(e, pageQuery);
            UserPermission userPermission = userPermissionService.getByUserPermission(UserPermission.builder()
                    .userId(e.getUserId())
                    .businessId(pageQuery.getBusinessId())
                    .businessType(pageQuery.getBusinessType())
                    .build());
            e.setUserPermission(BeanCopierUtil.copyProperties(userPermission, UserPermissionVO.UserPermission.class));
        }).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public List<AccessManagementVO.XAccessManagement> queryAmsUser(String username, String amType) {
        UserVO.User vo = UserVO.User.builder()
                .username(username)
                .build();
        return amPacker.toAms(vo, amType);
    }

    @Override
    public UserVO.UserMFA getUserMFA() {
        String username = SessionUtil.getUsername();
        User user = userService.getByUsername(username);
        return createMfa(user);
    }

    @Transactional(rollbackFor = {CommonRuntimeException.class, Exception.class})
    @Override
    public UserVO.UserMFA resetUserMFA() {
        String username = SessionUtil.getUsername();
        User user = userService.getByUsername(username);
        if (user.getForceMfa())
            throw new CommonRuntimeException("MFA无法重置: 管理员强制启用!");
        if (user.getMfa()) {
            User userMfa = User.builder()
                    .id(user.getId())
                    .mfa(false)
                    .build();
            userService.updateMfa(userMfa);
            user.setMfa(false);
        }
        List<UserCredential> credentials = userCredentialService.queryByUserIdAndType(user.getId(), UserCredentialTypeEnum.OTP_SK.getType());
        if (!CollectionUtils.isEmpty(credentials)) {
            credentials.forEach(e -> userCredentialService.deleteById(e.getId()));
        }
        return createMfa(user);
    }

    @Transactional(rollbackFor = {CommonRuntimeException.class, Exception.class})
    @Override
    public UserVO.UserMFA bindUserMFA(String otp) {
        String username = SessionUtil.getUsername();
        User user = userService.getByUsername(username);
        if (user.getMfa())
            throw new CommonRuntimeException("MFA无法绑定: 重复操作!");
        mfaAuthHelper.verify(user, LoginParam.Login.builder().otp(otp).build());
        user.setMfa(true);
        userService.updateMfa(user);
        return createMfa(user);
    }

    @Override
    public UserVO.UserIAMMFA getUserIAMMFA() {
        String username = SessionUtil.getUsername();
        User user = userService.getByUsername(username);
        final String defQrcode = "otpauth://totp/Amazon%20Web%20Services:${NAME}?secret=${SECRET}&issuer=Amazon%20Web%20Services";
        List<MfaVO.MFA> userMfas = userCredentialService.queryByUserIdAndType(user.getId(), UserCredentialTypeEnum.IAM_OTP_SK.getType()).stream()
                .map(e -> {
                    String name = StringUtils.isNotBlank(e.getComment()) ? e.getComment() : username;
                    Map<String, String> dict = SimpleDictBuilder.newBuilder()
                            .putParam("NAME", name)
                            .putParam("SECRET", e.getCredential())
                            .build()
                            .getDict();
                    final String qrcode = TemplateUtil.render(defQrcode, dict);
                    return MfaVO.MFA.builder()
                            .title(e.getTitle())
                            .secret(e.getCredential())
                            .username(username)
                            .qrcode(qrcode)
                            .build();
                }).collect(Collectors.toList());
        return UserVO.UserIAMMFA.builder()
                .username(username)
                .mfa(CollectionUtils.isEmpty(userMfas))
                .userMfas(userMfas)
                .build();
    }

    private UserVO.UserMFA createMfa(User user) {
        MfaVO.MFA mfa;
        if (!user.getMfa()) {
            // 创建用户MFA凭据（不会重复申请）
            userCredentialFacade.createMFACredential(user);
            List<UserCredential> credentials = userCredentialService.queryByUserIdAndType(user.getId(), UserCredentialTypeEnum.OTP_SK.getType());
            String otpSk = credentials.get(0).getCredential();
            mfa = MfaVO.MFA.builder()
                    .username(user.getUsername())
                    .qrcode(OtpUtil.toQRCode(Joiner.on("@").join(user.getUsername(), "opscloud"), otpSk))
                    .secret(otpSk)
                    .build();
        } else {
            // 用户已经启用MFA不再显示
            mfa = MfaVO.MFA.NOT_SHOW;
        }
        return UserVO.UserMFA.builder()
                .id(user.getId())
                .username(user.getUsername())
                .mfa(user.getMfa()) // 是否启用MFA
                .forceMfa(user.getForceMfa())
                .userMfa(mfa)
                .build();
    }

}
