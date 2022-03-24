package com.offbytwo.jenkins.model;

/**
 * Represents build console log
 */
public class ConsoleLog {

    private String consoleLog;
    private Boolean hasMoreData;
    private Integer currentBufferSize;

    public ConsoleLog(String consoleLog, Boolean hasMoreData, Integer currentBufferSize) {
        this.consoleLog = consoleLog;
        this.hasMoreData = hasMoreData;
        this.currentBufferSize = currentBufferSize;
    }

    public String getConsoleLog() {
        return consoleLog;
    }

    public ConsoleLog setConsoleLog(String consoleLog) {
        this.consoleLog = consoleLog;
        return this;
    }

    public Boolean getHasMoreData() {
        return hasMoreData;
    }

    public ConsoleLog setHasMoreData(Boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
        return this;
    }

    public Integer getCurrentBufferSize() {
        return currentBufferSize;
    }

    public ConsoleLog setCurrentBufferSize(Integer currentBufferSize) {
        this.currentBufferSize = currentBufferSize;
        return this;
    }
}
