package com.baiyi.caesar.util;

import com.baiyi.caesar.common.type.ProtocolEnum;
import com.baiyi.caesar.domain.types.BusinessTypeEnum;
import com.baiyi.caesar.domain.vo.common.OptionsVO;
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
            OptionsVO.Option o = OptionsVO.Option.builder()
                    .label(e.getName())
                    .value(e.getType())
                    .build();
            optionList.add(o);
        }
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }

    public static OptionsVO.Options toProtocolOptions() {
        List<OptionsVO.Option> optionList = Lists.newArrayList();
        for (ProtocolEnum e : ProtocolEnum.values()) {
            OptionsVO.Option o = OptionsVO.Option.builder()
                    .label(e.getType())
                    .value(e.getType())
                    .build();
            optionList.add(o);
        }
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }
}
