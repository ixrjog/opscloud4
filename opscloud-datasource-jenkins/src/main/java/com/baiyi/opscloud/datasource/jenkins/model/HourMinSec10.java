package com.baiyi.opscloud.datasource.jenkins.model;

/**
 * @author Karl Heinz Marbaise
 *
 *         TODO: Has someone a better name for the class?
 */
public class HourMinSec10 {

    private Statis hour;
    private Statis min;
    private Statis sec10;

    public Statis getHour() {
        return hour;
    }

    public HourMinSec10 setHour(Statis hour) {
        this.hour = hour;
        return this;
    }

    public Statis getMin() {
        return min;
    }

    public HourMinSec10 setMin(Statis min) {
        this.min = min;
        return this;
    }

    public Statis getSec10() {
        return sec10;
    }

    public HourMinSec10 setSec10(Statis sec10) {
        this.sec10 = sec10;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hour == null) ? 0 : hour.hashCode());
        result = prime * result + ((min == null) ? 0 : min.hashCode());
        result = prime * result + ((sec10 == null) ? 0 : sec10.hashCode());
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
        HourMinSec10 other = (HourMinSec10) obj;
        if (hour == null) {
            if (other.hour != null)
                return false;
        } else if (!hour.equals(other.hour))
            return false;
        if (min == null) {
            if (other.min != null)
                return false;
        } else if (!min.equals(other.min))
            return false;
        if (sec10 == null) {
            return other.sec10 == null;
        } else return sec10.equals(other.sec10);
    }

}