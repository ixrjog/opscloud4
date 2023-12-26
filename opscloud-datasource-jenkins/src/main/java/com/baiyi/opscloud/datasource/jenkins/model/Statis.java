/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.opscloud.datasource.jenkins.model;

import lombok.Getter;

import java.util.List;

/**
 * @author Karl Heinz Marbaise
 *
 *         TODO: Has someone a better name for the class?
 */
@Getter
public class Statis {

    private List<Double> history;
    private Double latest;

    public Statis setHistory(List<Double> history) {
        this.history = history;
        return this;
    }

    public Statis setLatest(Double latest) {
        this.latest = latest;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((history == null) ? 0 : history.hashCode());
        result = prime * result + ((latest == null) ? 0 : latest.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Statis other = (Statis) obj;
        if (history == null) {
            if (other.history != null)
                return false;
        } else if (!history.equals(other.history))
            return false;
        if (latest == null) {
            return other.latest == null;
        } else return latest.equals(other.latest);
    }

}