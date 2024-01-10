package com.baiyi.opscloud.datasource.jenkins.model;

import lombok.Getter;

@Getter
public class JacocoCoverageResult {

    private int covered;
    private int missed;
    private int percentage;
    private int percentageFloat;
    private int total;

    public JacocoCoverageResult setCovered(int covered) {
        this.covered = covered;
        return this;
    }

    public JacocoCoverageResult setMissed(int missed) {
        this.missed = missed;
        return this;
    }

    public JacocoCoverageResult setPercentage(int percentage) {
        this.percentage = percentage;
        return this;
    }

    public JacocoCoverageResult setPercentageFloat(int percentageFloat) {
        this.percentageFloat = percentageFloat;
        return this;
    }

    public JacocoCoverageResult setTotal(int total) {
        this.total = total;
        return this;
    }

}