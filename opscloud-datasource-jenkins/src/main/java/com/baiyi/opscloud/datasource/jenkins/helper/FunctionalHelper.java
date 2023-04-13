package com.baiyi.opscloud.datasource.jenkins.helper;

import com.baiyi.opscloud.datasource.jenkins.client.JenkinsHttpConnection;
import com.baiyi.opscloud.datasource.jenkins.model.BaseModel;

import java.util.function.Function;

public final class FunctionalHelper {

    private FunctionalHelper() {
        // intentionally empty.
    }

    public static <T extends BaseModel> Function<T, T> SET_CLIENT(JenkinsHttpConnection client) {
        return s -> {
            s.setClient(client);
            return s;
        };
    }

}