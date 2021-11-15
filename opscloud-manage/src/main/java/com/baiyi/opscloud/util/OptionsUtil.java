package com.baiyi.opscloud.util;

import com.baiyi.opscloud.common.constant.enums.ProtocolEnum;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.common.OptionsVO;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/24 6:26 下午
 * @Version 1.0
 */
public class OptionsUtil {

    public static OptionsVO.Options toBusinessTypeOptions() {
        List<OptionsVO.Option> optionList = Lists.newArrayList();
        for (BusinessTypeEnum e : BusinessTypeEnum.values()) {
            optionList.add(OptionsVO.Option.builder()
                    .label(e.getName())
                    .value(e.getType())
                    .build());
        }
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }

    public static OptionsVO.Options toApplicationBusinessTypeOptions() {
        List<OptionsVO.Option> optionList = Lists.newArrayList();
        for (BusinessTypeEnum e : BusinessTypeEnum.values()) {
            if (!e.isInApplication()) continue;
            optionList.add(OptionsVO.Option.builder()
                    .label(e.getName())
                    .value(e.getType())
                    .build());
        }
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }

    public static OptionsVO.Options toProtocolOptions() {
        List<OptionsVO.Option> optionList = Lists.newArrayList();
        for (ProtocolEnum e : ProtocolEnum.values()) {
            optionList.add(OptionsVO.Option.builder()
                    .label(e.getType())
                    .value(e.getType())
                    .build());
        }
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }
}
