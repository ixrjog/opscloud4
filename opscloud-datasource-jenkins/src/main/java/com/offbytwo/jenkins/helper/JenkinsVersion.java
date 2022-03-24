/*
 * Copyright (c) 2016 Karl Heinz Marbaise
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */
package com.offbytwo.jenkins.helper;

/**
 * @author Karl Heinz Marbaise
 */
public class JenkinsVersion implements Comparable<JenkinsVersion> {
    private ComparableVersion cv;
    private String literalVersion;

    public final static JenkinsVersion create(String version) {
        JenkinsVersion jv = new JenkinsVersion(version);
        return jv;
    }

    public JenkinsVersion() {
        this.cv = new ComparableVersion("0");
    }

    public JenkinsVersion(String version) {
        this.literalVersion = version;
        this.cv = new ComparableVersion(version);
    }

    /**
     * This will check if the current instance version is <code>&gt;</code> the
     * given version.
     * 
     * @param version The version to compare with.
     * @return true or false.
     */
    public boolean isGreaterThan(String version) {
        JenkinsVersion create = create(version);
        return this.cv.compareTo(create.cv) > 0;
    }

    public boolean isGreaterThan(JenkinsVersion jv) {
        return this.cv.compareTo(jv.cv) > 0;
    }

    /**
     * This will check if the current instance version is <code>&gt;=</code> the
     * given version.
     * 
     * @param version The version to compare with.
     * @return true or false.
     */
    public boolean isGreaterOrEqual(String version) {
        JenkinsVersion create = create(version);
        return this.cv.compareTo(create.cv) >= 0;
    }

    public boolean isGreaterOrEqual(JenkinsVersion jv) {
        return this.cv.compareTo(jv.cv) >= 0;
    }

    /**
     * This will check if the current instance version is <code>&lt;</code> the
     * given version.
     * 
     * @param version The version to compare with.
     * @return true or false.
     */
    public boolean isLessThan(String version) {
        JenkinsVersion create = create(version);
        return this.cv.compareTo(create.cv) < 0;
    }

    public boolean isLessThan(JenkinsVersion jv) {
        return this.cv.compareTo(jv.cv) < 0;
    }

    /**
     * This will check if the current instance version is <code>&lt;=</code> the
     * given version.
     * 
     * @param version The version to compare with.
     * @return true or false.
     */
    public boolean isLessOrEqual(String version) {
        JenkinsVersion create = create(version);
        return this.cv.compareTo(create.cv) <= 0;
    }

    public boolean isLessOrEqual(JenkinsVersion jv) {
        return this.cv.compareTo(jv.cv) <= 0;
    }

    /**
     * This will check if the current instance version is <code>=</code> the
     * given version.
     * 
     * @param version The version to compare with.
     * @return true or false.
     */
    public boolean isEqualTo(String version) {
        JenkinsVersion create = create(version);
        return this.cv.compareTo(create.cv) == 0;
    }

    public boolean isEqualTo(JenkinsVersion jv) {
        return this.cv.compareTo(jv.cv) == 0;
    }

    @Override
    public int compareTo(JenkinsVersion o) {
        return this.compareTo(o);
    }

    public String getLiteralVersion() {
        return literalVersion;
    }

    @Override
    public String toString() {
        return literalVersion;
    }
}