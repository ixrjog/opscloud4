package com.baiyi.opscloud.leo.packer;

import com.baiyi.opscloud.common.annotation.AgoWrapper;
import com.baiyi.opscloud.common.annotation.RuntimeWrapper;
import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuildImage;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuildLog;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.domain.vo.leo.LeoLogVO;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.delegate.JenkinsPipelineDelegate;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.service.leo.LeoBuildImageService;
import com.baiyi.opscloud.service.leo.LeoBuildLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/11/24 17:46
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeoBuildResponsePacker {

    private final LeoBuildImageService leoBuildImageService;

    private final JenkinsPipelineDelegate jenkinsPipelineDelegate;

    private final LeoBuildLogService leoBuildLogService;

    @AgoWrapper(extend = true)
    @RuntimeWrapper(extend = true)
    @TagsWrapper(extend = true)
    public void wrap(LeoBuildVO.Build build) {
        LeoBuildModel.BuildConfig buildConfig = LeoBuildModel.load(build.getBuildConfig());
        build.setBuildDetails(buildConfig);
        LeoBuildVO.Pipeline pipeline;
        if (build.getIsFinish()) {
            pipeline = jenkinsPipelineDelegate.getPipelineByCache(build, buildConfig);
        } else {
            pipeline = jenkinsPipelineDelegate.getPipeline(build, buildConfig);
        }
        build.setPipeline(pipeline);
        Map<String, String> dict = buildConfig.getBuild().getDict();
        if (dict.containsKey(BuildDictConstants.IMAGE.getKey())) {
            final String image = dict.get(BuildDictConstants.IMAGE.getKey());
            LeoBuildImage leoBuildImage = leoBuildImageService.getByUniqueKey(build.getId(), image);
            build.setIsImageExists(leoBuildImage != null);
        } else {
            build.setIsImageExists(false);
        }
        wrapLogs(build);
    }

    private void wrapLogs(LeoBuildVO.Build build) {
        build.setBuildLogs(leoBuildLogService.queryByBuildId(build.getId()).stream()
                .map(this::toLog)
                .collect(Collectors.toList())
        );
    }

    private LeoLogVO.Log toLog(LeoBuildLog leoBuildLog) {
        return LeoLogVO.Log.builder()
                .id(leoBuildLog.getId())
                .level(leoBuildLog.getLogLevel())
                .log(leoBuildLog.getLog())
                .createTime(leoBuildLog.getCreateTime())
                .build();
    }

}