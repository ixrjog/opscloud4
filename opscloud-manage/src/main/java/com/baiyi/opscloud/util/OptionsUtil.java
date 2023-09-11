package com.baiyi.opscloud.util;

import com.baiyi.opscloud.common.constants.enums.ProtocolEnum;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Tag;
import com.baiyi.opscloud.domain.vo.common.OptionsVO;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/24 6:26 下午
 * @Version 1.0
 */
public class OptionsUtil {

    public static OptionsVO.Options toBusinessTypeOptions() {
        List<OptionsVO.Option> optionList = Arrays.stream(BusinessTypeEnum.values()).map(e -> OptionsVO.Option.builder()
                .label(e.name())
                .value(e.getType())
                .comment(e.getName())
                .build()).collect(Collectors.toList());
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }

    public static OptionsVO.Options toApplicationBusinessTypeOptions() {
        List<OptionsVO.Option> optionList = Arrays.stream(BusinessTypeEnum.values()).filter(BusinessTypeEnum::isInApplication).map(e -> OptionsVO.Option.builder()
                .label(e.getName())
                .value(e.getType())
                .comment(e.getName())
                .build()).collect(Collectors.toList());
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }

    public static OptionsVO.Options toProtocolOptions() {
        List<OptionsVO.Option> optionList = Arrays.stream(ProtocolEnum.values()).map(e -> OptionsVO.Option.builder()
                .label(e.getType())
                .value(e.getType())
                .build()).collect(Collectors.toList());
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }

    public static OptionsVO.Options toProjectBusinessTypeOptions() {
        List<OptionsVO.Option> optionList = Lists.newArrayList(BusinessTypeEnum.ASSET)
                .stream().map(e -> OptionsVO.Option.builder()
                        .label(e.getName())
                        .value(e.getType())
                        .comment(e.getName())
                        .build()).collect(Collectors.toList());
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }

    public static OptionsVO.Options toOptions(List<String> strings) {
        List<OptionsVO.Option> optionList = strings.stream().map(e -> OptionsVO.Option.builder()
                .label(e)
                .value(e)
                .build()).collect(Collectors.toList());
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }

    public static OptionsVO.Options toFinOpsTagOptions(List<Tag> tags) {
        List<OptionsVO.Option> optionList = tags.stream().map(e -> OptionsVO.Option.builder()
                .label(e.getTagKey())
                .value(e.getTagKey())
                .comment(e.getComment())
                .build()).collect(Collectors.toList());
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }


}
