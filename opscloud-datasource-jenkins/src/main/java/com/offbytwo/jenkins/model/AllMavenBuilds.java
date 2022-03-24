/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import com.offbytwo.jenkins.model.BaseModel;
import com.offbytwo.jenkins.model.MavenBuild;
import com.offbytwo.jenkins.model.MavenJobWithDetails;

import java.util.List;

/**
 * This class is only needed to get all builds in
 * {@link MavenJobWithDetails#getAllBuilds()}.
 * 
 * @author Karl Heinz Marbaise
 *
 *         NOTE: This class is not part of any public API
 */
class AllMavenBuilds extends BaseModel {
    private List<MavenBuild> allBuilds;

    public AllMavenBuilds() {
    }

    public List<MavenBuild> getAllBuilds() {
        return this.allBuilds;
    }
}