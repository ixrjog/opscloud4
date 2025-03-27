package com.baiyi.opscloud.datasource.huaweicloud.ecs.util;

import com.baiyi.opscloud.common.datasource.HuaweicloudConfig;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/18 14:43
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class HuaweicloudProjectUtil {

    private static final String NO_PROJECT_ID = null;

    public static String findProjectId(String regionId, HuaweicloudConfig.Huaweicloud huaweicloud) {
        if (!CollectionUtils.isEmpty(huaweicloud.getProjects())) {
            Optional<HuaweicloudConfig.Project> optionalProject = huaweicloud.getProjects()
                    .stream()
                    .filter(e -> regionId.equals(e.getName()))
                    .findFirst();
            if (optionalProject.isPresent()) {
                return optionalProject.get()
                        .getId();
            }
        }
        return NO_PROJECT_ID;
    }

}
