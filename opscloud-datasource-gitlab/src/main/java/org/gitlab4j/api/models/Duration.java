package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.gitlab4j.api.utils.DurationUtils;

/**
 * This class represents a duration in time.
 */
public class Duration {

    private int seconds;
    private String durationString;

    /**
     * Create a Duration instance from a human readable string. e.g: 3h30m
     *
     * @param durationString a duration in human readable format
     */
    public Duration(String durationString) {
        seconds = DurationUtils.parse(durationString);
        this.durationString = (seconds == 0 ? "0m" : DurationUtils.toString(seconds));
    }

    /**
     * Create a Duration instance from a number of seconds.
     *
     * @param seconds the number of seconds for this Duration instance to represent
     */
    public Duration(int seconds) {
        this.seconds = seconds;
        durationString = (seconds == 0 ? "0m" : DurationUtils.toString(seconds));
    }

    /**
     * Get the number of seconds this duration represents.
     *
     * @return the number of seconds this duration represents
     */
    public int getSeconds() {
        return (seconds);
    }

    /**
     * Set the number of seconds this duration represents.
     *
     * @param seconds the number of seconds this duration represents
     */
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    @JsonValue
    @Override
    public String toString() {
        return (durationString);
    }

    @JsonCreator
    public static Duration forValue(String value) {
        return new Duration(value);
    }
}
