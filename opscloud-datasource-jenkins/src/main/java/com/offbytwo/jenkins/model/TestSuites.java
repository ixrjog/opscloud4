package com.offbytwo.jenkins.model;

import java.util.List;

/**
 * @author Karl Heinz Marbaise
 *
 */
public class TestSuites {

    private double duration;
    private String id;
    private String name;
    private String timestamp;

    private List<TestCase> cases;

    public double getDuration() {
        return duration;
    }

    public TestSuites setDuration(double duration) {
        this.duration = duration;
        return this;
    }

    public String getId() {
        return id;
    }

    public TestSuites setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TestSuites setName(String name) {
        this.name = name;
        return this;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public TestSuites setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public List<TestCase> getCases() {
        return cases;
    }

    public TestSuites setCases(List<TestCase> cases) {
        this.cases = cases;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cases == null) ? 0 : cases.hashCode());
        long temp;
        temp = Double.doubleToLongBits(duration);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
        TestSuites other = (TestSuites) obj;
        if (cases == null) {
            if (other.cases != null)
                return false;
        } else if (!cases.equals(other.cases))
            return false;
        if (Double.doubleToLongBits(duration) != Double.doubleToLongBits(other.duration))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        return true;
    }

}
