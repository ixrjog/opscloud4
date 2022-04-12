package com.baiyi.opscloud.datasource.jenkins.model;

/**
 * @author Karl Heinz Marbaise
 *
 */
public class TestChildReport {

    private TestChild child;
    private TestResult result;

    public TestChild getChild() {
        return child;
    }

    public TestChildReport setChild(TestChild child) {
        this.child = child;
        return this;
    }

    public TestResult getResult() {
        return result;
    }

    public TestChildReport setResult(TestResult result) {
        this.result = result;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((child == null) ? 0 : child.hashCode());
        result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
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
        TestChildReport other = (TestChildReport) obj;
        if (child == null) {
            if (other.child != null)
                return false;
        } else if (!child.equals(other.child))
            return false;
        if (result == null) {
            if (other.result != null)
                return false;
        } else if (!result.equals(other.result))
            return false;
        return true;
    }

}
