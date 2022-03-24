package com.offbytwo.jenkins.helper;

/**
 * Listener interface used to obtain build console logs
 */
public interface BuildConsoleStreamListener {

    /**
     * Called by api when new log data available.
     *
     * @param newLogChunk - string containing latest chunk of logs
     */
    void onData(String newLogChunk);

    /**
     * Called when streaming console output is finished
     */
     void finished();
}
