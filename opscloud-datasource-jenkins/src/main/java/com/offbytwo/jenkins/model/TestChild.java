package com.offbytwo.jenkins.model;

/**
 * @author Karl Heinz Marbaise
 *
 */
public class TestChild {
    private int number;
    private String url;

    public int getNumber() {
        return number;
    }

    public TestChild setNumber(int number) {
        this.number = number;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public TestChild setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + number;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
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
        TestChild other = (TestChild) obj;
        if (number != other.number)
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

}
