package com.baiyi.opscloud.sshserver.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @Author 修远
 * @Date 2021/6/10 11:08 上午
 * @Since 1.0
 */

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class SshAuthentication {

    @NonNull
    private final String name;

    @NonNull
    private final Object principal;

    private Object details;

    private Object credentials;

    private List<String> authorities;
}
