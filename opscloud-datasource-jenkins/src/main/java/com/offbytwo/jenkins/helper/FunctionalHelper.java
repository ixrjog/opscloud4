package com.offbytwo.jenkins.helper;

import com.offbytwo.jenkins.client.JenkinsHttpConnection;
import com.offbytwo.jenkins.model.BaseModel;

import java.util.function.Function;

public final class FunctionalHelper {

    private FunctionalHelper() {
        // intentionally empty.
    }

    public static final <T extends BaseModel> Function<T, T> SET_CLIENT(JenkinsHttpConnection client) {
        return s -> {
            s.setClient(client);
            return s;
        };
    }


}
