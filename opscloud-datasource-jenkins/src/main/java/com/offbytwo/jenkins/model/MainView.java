/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainView extends BaseModel {

    private List<com.offbytwo.jenkins.model.Job> jobs;
    private List<com.offbytwo.jenkins.model.View> views;

    /* default constructor needed for Jackson */
    public MainView() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public MainView(List<com.offbytwo.jenkins.model.Job> jobs, List<com.offbytwo.jenkins.model.View> views) {
        this.jobs = jobs;
        this.views = views;
    }

    public MainView(com.offbytwo.jenkins.model.Job... jobs) {
        this(Arrays.asList(jobs), new ArrayList<>());
    }

    public List<com.offbytwo.jenkins.model.Job> getJobs() {
        return jobs;
    }

    public MainView setJobs(List<Job> jobs) {
        this.jobs = jobs;
        return this;
    }

    public List<com.offbytwo.jenkins.model.View> getViews() {
        return views;
    }

    public MainView setViews(List<View> views) {
        this.views = views;
        return this;
    }
}
