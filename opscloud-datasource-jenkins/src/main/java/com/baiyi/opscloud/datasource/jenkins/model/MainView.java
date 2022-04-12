/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.opscloud.datasource.jenkins.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainView extends BaseModel {

    private List<Job> jobs;
    private List<View> views;

    /* default constructor needed for Jackson */
    public MainView() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public MainView(List<Job> jobs, List<View> views) {
        this.jobs = jobs;
        this.views = views;
    }

    public MainView(Job... jobs) {
        this(Arrays.asList(jobs), new ArrayList<>());
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public MainView setJobs(List<Job> jobs) {
        this.jobs = jobs;
        return this;
    }

    public List<View> getViews() {
        return views;
    }

    public MainView setViews(List<View> views) {
        this.views = views;
        return this;
    }
}
