package com.baiyi.opscloud.leo.message.util;

import com.baiyi.opscloud.domain.constants.VersionTypeConstants;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVersionVO;
import com.google.common.collect.Maps;
import org.glassfish.jersey.internal.guava.Sets;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

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
        deploymentVersions.stream().map(LeoJobVersionVO.DeploymentVersion::getBuildId).filter(buildId -> buildId != -1).forEach(idSet::add);
        // 未知版本, 只设置OFFLINE
        deploymentVersions.forEach(d -> {
            if (d.getReplicas() == 0) {
                d.setVersionType(VersionTypeConstants.OFFLINE.name());
            }
        });
        if (idSet.isEmpty()) {
            return;
        }
        Map<String, Integer> versionTypeMap = Maps.newHashMap();
        List<Integer> ids = idSet.stream().sorted(Collections.reverseOrder()).toList();
        int bound = ids.size();
        IntStream.range(0, bound).forEach(i -> {
            int buildId = ids.get(i);
            for (LeoJobVersionVO.DeploymentVersion deploymentVersion : deploymentVersions) {
                if (deploymentVersion.getBuildId() != buildId) {
                    continue;
                }
                if (deploymentVersion.getReplicas() == 0) {
                    continue;
                }
                switch (i) {
                    case 0 -> {
                        versionTypeMap.put(VersionTypeConstants.BLUE.name(), deploymentVersion.getBuildId());
                        deploymentVersion.setVersionType(VersionTypeConstants.BLUE.name());
                    }
                    case 1 -> {
                        versionTypeMap.put(VersionTypeConstants.GREEN.name(), deploymentVersion.getBuildId());
                        deploymentVersion.setVersionType(VersionTypeConstants.GREEN.name());
                    }
                    default -> deploymentVersion.setVersionType(VersionTypeConstants.OTHER.name());
                }
            }
        });
        render(deploymentVersions, versionTypeMap);
    }

    private static void render(List<LeoJobVersionVO.DeploymentVersion> deploymentVersions, Map<String, Integer> versionTypeMap) {
        deploymentVersions.forEach(d -> {
            if (VersionTypeConstants.OTHER.name().equals(d.getVersionType()) || VersionTypeConstants.GREEN.name().equals(d.getVersionType())) {
                if (versionTypeMap.containsKey(VersionTypeConstants.BLUE.name())) {
                    LeoJobVersionVO.DoDeployVersion doDeployVersion = LeoJobVersionVO.DoDeployVersion.builder()
                            .buildId(versionTypeMap.get(VersionTypeConstants.BLUE.name()))
                            .build();
                    d.setDoDeployVersion(doDeployVersion);
                } else {
                    d.setDoDeployVersion(LeoJobVersionVO.DoDeployVersion.INVALID);
                }
                return;
            }
            if (VersionTypeConstants.BLUE.name().equals(d.getVersionType())) {
                if (versionTypeMap.containsKey(VersionTypeConstants.GREEN.name())) {
                    LeoJobVersionVO.DoDeployVersion doDeployVersion = LeoJobVersionVO.DoDeployVersion.builder()
                            .buildId(versionTypeMap.get(VersionTypeConstants.GREEN.name()))
                            .build();
                    d.setDoDeployVersion(doDeployVersion);
                } else {
                    d.setDoDeployVersion(LeoJobVersionVO.DoDeployVersion.INVALID);
                }
            }
        });
    }

}