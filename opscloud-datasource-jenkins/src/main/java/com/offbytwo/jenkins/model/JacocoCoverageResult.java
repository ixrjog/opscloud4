package com.offbytwo.jenkins.model;

public class JacocoCoverageResult {

    private int covered;
    private int missed;
    private int percentage;
    private int percentageFloat;
    private int total;

    public int getCovered() {
        return covered;
    }

    public JacocoCoverageResult setCovered(int covered) {
        this.covered = covered;
        return this;
    }

    public int getMissed() {
        return missed;
    }

    public JacocoCoverageResult setMissed(int missed) {
        this.missed = missed;
        return this;
    }

    public int getPercentage() {
        return percentage;
    }

    public JacocoCoverageResult setPercentage(int percentage) {
        this.percentage = percentage;
        return this;
    }

    public int getPercentageFloat() {
        return percentageFloat;
    }

    public JacocoCoverageResult setPercentageFloat(int percentageFloat) {
        this.percentageFloat = percentageFloat;
        return this;
    }

    public int getTotal() {
        return total;
    }

    public JacocoCoverageResult setTotal(int total) {
        this.total = total;
        return this;
    }

}