/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.opscloud.datasource.jenkins.model;

import com.baiyi.opscloud.datasource.jenkins.client.JenkinsHttpConnection;

import java.util.function.Predicate;

/**
 * The base model.
 */
public class BaseModel {

    /**
     * The class.
     */
    private String _class;

    /**
     * Get the class.
     * @return class
     */
    public String get_class() {
        return _class;
    }

    //TODO: We should make this private
    protected JenkinsHttpConnection client;

    
    /**
     * Get the HTTP client.
     * @return client
     */
    public JenkinsHttpConnection getClient() {
        return client;
    }

    /**
     * Set the HTTP client.
     * @param client {@link JenkinsHttpConnection}.
     */
    public BaseModel setClient(final JenkinsHttpConnection client) {
        this.client = client;
        return this;
    }

    protected static Predicate<? super Build> isBuildNumberEqualTo(int buildNumber) {
        return build -> build.getNumber() == buildNumber;
    }

}