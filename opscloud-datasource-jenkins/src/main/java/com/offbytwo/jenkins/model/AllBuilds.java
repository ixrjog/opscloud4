/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import com.offbytwo.jenkins.model.BaseModel;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.JobWithDetails;

import java.util.List;

/**
 * This class is only needed to get all builds in
 * {@link JobWithDetails#getAllBuilds()}.
 * 
 * @author Karl Heinz Marbaise
 *
 *         NOTE: This class is not part of any public API
 */
class AllBuilds extends BaseModel {
    private List<Build> allBuilds;

    public AllBuilds() {
    }

    public List<Build> getAllBuilds() {
        return this.allBuilds;
    }
}