package com.baiyi.opscloud.leo.message.util;

import com.baiyi.opscloud.domain.constants.VersionTypeConstants;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVersionVO;
import org.glassfish.jersey.internal.guava.Sets;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/2/27 09:51
 * @Version 1.0
 */
public class VersionRenderer {

    private VersionRenderer() {
    }

    /**
     * 渲染版本
     *
     * @param deploymentVersions
     */
    public static void render(List<LeoJobVersionVO.DeploymentVersion> deploymentVersions) {
        if (CollectionUtils.isEmpty(deploymentVersions)) {
            return;
        }
        Set<Integer> idSet = Sets.newHashSet();
        for (LeoJobVersionVO.DeploymentVersion deploymentVersion : deploymentVersions) {
            if (deploymentVersion.getBuildId() == -1) {
                if (deploymentVersion.getReplicas() == 0) {
                    deploymentVersion.setVersionColor(LeoJobVersionVO.VersionColors.OFFLINE);
                } else {
                    // 无法比较版本号
                    deploymentVersion.setVersionColor(LeoJobVersionVO.VersionColors.OTHER);
                }
            } else {
                idSet.add(deploymentVersion.getBuildId());
            }
        }
        if (idSet.isEmpty()) {
            return;
        }
        List<Integer> ids = idSet.stream().sorted(Collections.reverseOrder()).collect(Collectors.toList());
        for (int i = 0; i < ids.size(); i++) {
            int buildId = ids.get(i);
            for (LeoJobVersionVO.DeploymentVersion deploymentVersion : deploymentVersions) {
                if (deploymentVersion.getBuildId() == buildId) {
                    if (deploymentVersion.getReplicas() == 0) {
                        deploymentVersion.setVersionColor(LeoJobVersionVO.VersionColors.OFFLINE);
                        deploymentVersion.setVersionType(VersionTypeConstants.OFFLINE.name());
                        continue;
                    }
                    switch (i) {
                        case 0:
                            deploymentVersion.setVersionColor(LeoJobVersionVO.VersionColors.BLUE);
                            deploymentVersion.setVersionType(VersionTypeConstants.BLUE.name());
                            break;
                        case 1:
                            deploymentVersion.setVersionColor(LeoJobVersionVO.VersionColors.GREEN);
                            deploymentVersion.setVersionType(VersionTypeConstants.GREEN.name());
                            break;
                        default:
                            deploymentVersion.setVersionColor(LeoJobVersionVO.VersionColors.OTHER);
                            deploymentVersion.setVersionType(VersionTypeConstants.OTHER.name());
                    }
                }
            }
        }
    }
}
