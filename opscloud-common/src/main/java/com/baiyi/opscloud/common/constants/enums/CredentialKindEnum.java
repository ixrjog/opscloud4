package com.baiyi.opscloud.common.constants.enums;

import com.baiyi.opscloud.domain.vo.common.OptionsVO;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/17 4:42 下午
 * @Version 1.0
 */
@Getter
public enum CredentialKindEnum {

    /**
     * 凭据类型
     */
    USERNAME_WITH_PASSWORD(1,"Username with password"),
    SSH_USERNAME_WITH_PRIVATE_KEY(2,"SSH Username with private key"),
    SSH_USERNAME_WITH_KEY_PAIR(3,"SSH Username with key pair"),
    TOKEN(4,"Token"),
    ACCESS_KEY(5,"Access key"),
    // Kubernetes Config 客户端配置文件
    KUBE_CONFIG(6,"Kubernetes kubeconfig"),
    // SSL 证书（Pem/Key）
    SSL_CERTIFICATES(7,"SSL certificates")
    ;

    private final int kind;

    private final String name;

    CredentialKindEnum(int kind, String name) {
        this.kind = kind;
        this.name = name;
    }

    public static String getName(int kind) {
        return Arrays.stream(CredentialKindEnum.values()).filter(typeEnum -> typeEnum.kind == kind).findFirst().map(CredentialKindEnum::getName).orElse("Null");
    }

    public static OptionsVO.Options toOptions(){
        List<OptionsVO.Option> optionList = Arrays.stream(CredentialKindEnum.values()).map(e -> OptionsVO.Option.builder()
                .label(e.getName())
                .value(e.getKind())
                .build()).collect(Collectors.toList());
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }

}