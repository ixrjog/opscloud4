/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.opscloud.datasource.jenkins.model;

import lombok.Getter;

import java.util.List;

@Getter
public class QueueItemActions extends BaseModel {
    private List<CauseAction> causes;

    public QueueItemActions setCauses(List<CauseAction> causes) {
        this.causes = causes;
        return this;
    }

}