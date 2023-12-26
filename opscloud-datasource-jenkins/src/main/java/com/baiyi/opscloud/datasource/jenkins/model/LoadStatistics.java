package com.baiyi.opscloud.datasource.jenkins.model;

import lombok.Getter;

/**
 * This class will contain the information about the load statistics which can
 * be extracted by using the following url:
 * 
 * http://server/computer/nodename/loadStatistics/api/json?pretty&amp;depth=3
 * 
 * @author Karl Heinz Marbaise
 *
 */
@Getter
public class LoadStatistics extends BaseModel {
    private HourMinSec10 busyExecutors;
    private HourMinSec10 queueLength;
    private HourMinSec10 totalExecutors;

    public LoadStatistics setBusyExecutors(HourMinSec10 busyExecutors) {
        this.busyExecutors = busyExecutors;
        return this;
    }

    public LoadStatistics setQueueLength(HourMinSec10 queueLength) {
        this.queueLength = queueLength;
        return this;
    }

    public LoadStatistics setTotalExecutors(HourMinSec10 totalExecutors) {
        this.totalExecutors = totalExecutors;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((busyExecutors == null) ? 0 : busyExecutors.hashCode());
        result = prime * result + ((queueLength == null) ? 0 : queueLength.hashCode());
        result = prime * result + ((totalExecutors == null) ? 0 : totalExecutors.hashCode());
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
        LoadStatistics other = (LoadStatistics) obj;
        if (busyExecutors == null) {
            if (other.busyExecutors != null)
                return false;
        } else if (!busyExecutors.equals(other.busyExecutors))
            return false;
        if (queueLength == null) {
            if (other.queueLength != null)
                return false;
        } else if (!queueLength.equals(other.queueLength))
            return false;
        if (totalExecutors == null) {
            return other.totalExecutors == null;
        } else return totalExecutors.equals(other.totalExecutors);
    }

}