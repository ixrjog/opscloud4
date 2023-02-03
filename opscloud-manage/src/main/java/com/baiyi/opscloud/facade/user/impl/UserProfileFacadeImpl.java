package com.baiyi.opscloud.facade.user.impl;

import com.baiyi.opscloud.common.constants.UserProfileKeyEnum;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.generator.opscloud.UserProfile;
import com.baiyi.opscloud.domain.vo.user.UserProfileVO;
import com.baiyi.opscloud.facade.user.UserProfileFacade;
import com.baiyi.opscloud.service.user.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/2/2 15:23
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserProfileFacadeImpl implements UserProfileFacade {

    private final UserProfileService userProfileService;

    @Override
    public UserProfileVO.Profiles getProfiles() {
        if (StringUtils.isBlank(SessionUtil.getUsername())) {
            return UserProfileVO.Profiles.builder().build();
        }
        List<UserProfile> profileList = userProfileService.queryByUsername(SessionUtil.getUsername());
        if (CollectionUtils.isEmpty(profileList)) {
            return UserProfileVO.Profiles.builder()
                    .username(SessionUtil.getUsername())
                    .build();
        }
        Map<String, String> profilesMap = profileList.stream().collect(Collectors.toMap(UserProfile::getProfileKey, UserProfile::getValue, (k1, k2) -> k1));
        UserProfileVO.Theme theme = UserProfileVO.Theme.builder()
                .foreground(of(profilesMap, UserProfileKeyEnum.TERMINAL_THEME_FOREGROUND.getKey()))
                .background(of(profilesMap, UserProfileKeyEnum.TERMINAL_THEME_BACKGROUND.getKey()))
                .cursor(of(profilesMap, UserProfileKeyEnum.TERMINAL_THEME_CURSOR.getKey()))
                .build();
        UserProfileVO.Terminal terminal = UserProfileVO.Terminal.builder()
                .rows(Integer.parseInt(of(profilesMap, UserProfileKeyEnum.TERMINAL_ROWS.getKey())))
                .theme(theme)
                .build();
        return UserProfileVO.Profiles.builder()
                .username(SessionUtil.getUsername())
                .terminal(terminal)
                .build();
    }

    private String of(Map<String, String> profilesMap, String key) {
        if (profilesMap.containsKey(key)) {
            return profilesMap.get(key);
        }
        UserProfileKeyEnum userProfileKeyEnum = UserProfileKeyEnum.of(key);
        return userProfileKeyEnum == null ? "" : userProfileKeyEnum.getValue();
    }

}
