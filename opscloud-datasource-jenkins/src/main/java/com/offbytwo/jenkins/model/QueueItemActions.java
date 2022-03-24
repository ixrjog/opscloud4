/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.util.List;

public class QueueItemActions extends BaseModel {
    private List<com.offbytwo.jenkins.model.CauseAction> causes;

    public List<com.offbytwo.jenkins.model.CauseAction> getCauses() {
        return causes;
    }

    public QueueItemActions setCauses(List<CauseAction> causes) {
        this.causes = causes;
        return this;
    }

}
