package com.baiyi.opscloud.packer.leo;

import com.baiyi.opscloud.common.annotation.AgoWrapper;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuildImage;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.leo.LeoBuildImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/12/5 10:15
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LeoBuildVersionPacker implements IWrapper<LeoBuildVO.Build> {

    private final LeoBuildImageService leoBuildImageService;

    @Override
    @AgoWrapper
    public void wrap(LeoBuildVO.Build build, IExtend iExtend) {
        if (iExtend.getExtend()) {
            LeoBuildModel.BuildConfig buildConfig = LeoBuildModel.load(build.getBuildConfig());
            Map<String, String> dict = buildConfig.getBuild().getDict();
            if (!dict.containsKey(BuildDictConstants.IMAGE.getKey())) {
                return;
            }
            final String imageName = dict.get(BuildDictConstants.IMAGE.getKey());
            LeoBuildImage leoBuildImage = leoBuildImageService.getByUniqueKey(build.getId(), imageName);
            if (leoBuildImage == null) {
                return;
            }
            LeoBuildVO.Image image = BeanCopierUtil.copyProperties(leoBuildImage, LeoBuildVO.Image.class);
            build.setImage(image);
        }
    }

}
