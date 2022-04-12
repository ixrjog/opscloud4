package com.baiyi.opscloud.datasource.jenkins.model;

import java.util.List;

/**
 * @author Karl Heinz Marbaise
 *
 */
public class TestResult extends BaseModel {

    private double duration;
    private boolean empty;
    private int failCount;
    private int passCount;
    private int skipCount;

    private List<TestSuites> suites;

    public double getDuration() {
        return duration;
    }

    public TestResult setDuration(double duration) {
        this.duration = duration;
        return this;
    }

    public boolean isEmpty() {
        return empty;
    }

    public TestResult setEmpty(boolean empty) {
        this.empty = empty;
        return this;
    }

    public int getFailCount() {
        return failCount;
    }

    public TestResult setFailCount(int failCount) {
        this.failCount = failCount;
        return this;
    }

    public int getPassCount() {
        return passCount;
    }

    public TestResult setPassCount(int passCount) {
        this.passCount = passCount;
        return this;
    }

    public int getSkipCount() {
        return skipCount;
    }

    public TestResult setSkipCount(int skipCount) {
        this.skipCount = skipCount;
        return this;
    }

    public List<TestSuites> getSuites() {
        return suites;
    }

    public TestResult setSuites(List<TestSuites> suites) {
        this.suites = suites;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(duration);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + (empty ? 1231 : 1237);
        result = prime * result + failCount;
        result = prime * result + passCount;
        result = prime * result + skipCount;
        result = prime * result + ((suites == null) ? 0 : suites.hashCode());
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
        TestResult other = (TestResult) obj;
        if (Double.doubleToLongBits(duration) != Double.doubleToLongBits(other.duration))
            return false;
        if (empty != other.empty)
            return false;
        if (failCount != other.failCount)
            return false;
        if (passCount != other.passCount)
            return false;
        if (skipCount != other.skipCount)
            return false;
        if (suites == null) {
            if (other.suites != null)
                return false;
        } else if (!suites.equals(other.suites))
            return false;
        return true;
    }
}
