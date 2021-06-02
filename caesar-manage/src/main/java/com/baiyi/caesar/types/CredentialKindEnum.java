package com.baiyi.caesar.types;

import com.baiyi.caesar.domain.vo.common.OptionsVO;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/17 4:42 下午
 * @Version 1.0
 */
public enum CredentialKindEnum {

    USERNAME_WITH_PASSWORD(1,"Username with password"),
    SSH_USERNAME_WITH_PRIVATE_KEY(2,"SSH Username with private key"),
    SSH_USERNAME_WITH_KEY_PAIR(3,"SSH Username with key pair"),
    TOKEN(4,"Token")
    ;

    private int kind;
    private String name;

    CredentialKindEnum(int kind, String name) {
        this.kind = kind;
        this.name = name;
    }

    public static String getName(int kind) {
        for (CredentialKindEnum typeEnum : CredentialKindEnum.values())
            if (typeEnum.kind == kind)
                return typeEnum.getName();
        return "Null";
    }

    public int getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public static OptionsVO.Options toOptions(){
        List<OptionsVO.Option> optionList = Lists.newArrayList();
        for (CredentialKindEnum e : CredentialKindEnum.values()) {
            OptionsVO.Option o = OptionsVO.Option.builder()
                    .label(e.getName())
                    .value(e.getKind())
                    .build();
            optionList.add(o);
        }
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }
}
